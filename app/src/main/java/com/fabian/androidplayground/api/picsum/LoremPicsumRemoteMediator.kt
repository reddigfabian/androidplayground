package com.fabian.androidplayground.api.picsum

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fabian.androidplayground.db.common.RemoteKeys
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "LPRemoteMediator"

@ExperimentalPagingApi
class LoremPicsumRemoteMediator(private val db : LoremPicsumDatabase) : RemoteMediator<Int, Picsum>() {
    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Picsum>): MediatorResult {
        if (loadType != LoadType.PREPEND) Log.d(TAG, "=================================== STARTING LOAD ===================================")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d(TAG, "LoadType : REFRESH")
                    val remoteKeys = getClosestRemoteKey(state)
                    remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    Log.d(TAG, "LoadType : APPEND")
                    val remoteKeys = getLastRemoteKey(state) ?: return MediatorResult.Success(false)
                    remoteKeys.nextKey ?: 0
                }
            }
            Log.d(TAG, "Page: $page")
            var endOfPaginationReached = false
            if (page > 0) {
                val response = LoremPicsumApi.loremPicsumService.imageList(state.config.pageSize, page)
                endOfPaginationReached = response.size < state.config.pageSize

                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        db.getRemoteKeysDao().clearRemoteKeys()
                        db.getPicsumDao().clearAll()
                    }

                    val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = response.map {
                        RemoteKeys(it.id, prevKey, nextKey)
                    }

                    db.getRemoteKeysDao().insertAll(keys)
                    db.getPicsumDao().insertAll(response)
                }
            }
            MediatorResult.Success(endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } finally {
            Log.d(TAG, "=================================== FINISHED LOAD ===================================")
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Picsum>): RemoteKeys? {
        Log.d(TAG, "getLastRemoteKey: lastItem = ${state.lastItemOrNull()}")
        val let = state.lastItemOrNull()?.let { picsum ->
            db.withTransaction {
                db.getRemoteKeysDao().remoteKeysPicsumID(picsum.id)
            }
        }
        Log.d(TAG, "getLastRemoteKey: $let")
        return let
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Picsum>): RemoteKeys? {
        Log.d(TAG, "getClosestRemoteKey: anchorPosition = ${state.anchorPosition}")
        val let = state.anchorPosition?.let { position ->
            Log.d(TAG, "getClosestRemoteKey: closestItem = ${state.closestItemToPosition(position)}")
            state.closestItemToPosition(position)?.id?.let { index ->
                db.withTransaction { db.getRemoteKeysDao().remoteKeysPicsumID(index) }
            }
        }
        Log.d(TAG, "getClosestRemoteKey: $let")
        return let
    }
}