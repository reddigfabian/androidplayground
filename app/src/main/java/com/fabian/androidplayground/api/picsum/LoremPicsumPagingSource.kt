package com.fabian.androidplayground.api.picsum

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState

private const val TAG = "LoremPicsumPagingSource"
private const val PICSUM_STARTING_PAGE = 0

class LoremPicsumPagingSource() : PagingSource<Int, Picsum>()  {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Picsum> {
        return try {
            val page = if (params is LoadParams.Append) params.key else PICSUM_STARTING_PAGE
            val limit = params.loadSize
            val picsumResponse = LoremPicsumApi.loremPicsumService.imageListAsync(limit = limit, page = page).await()
            val nextKey = if (picsumResponse.isEmpty()) {
                null
            } else {
                page + (params.loadSize / PAGE_SIZE)
            }

            LoadResult.Page(
                data = picsumResponse,
                prevKey = if (page == PICSUM_STARTING_PAGE) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message?:"Exception" )
            LoadResult.Error(e)
        }
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, Picsum>): Int {
        return state.anchorPosition?: PICSUM_STARTING_PAGE
    }
}