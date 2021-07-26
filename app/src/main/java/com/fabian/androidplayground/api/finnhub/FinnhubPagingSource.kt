package com.fabian.androidplayground.api.finnhub

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect

private const val FINNHUB_STARTING_INDEX = 0

class FinnhubPagingSource : PagingSource<Int, FinnhubStockSymbol>()  {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FinnhubStockSymbol> {
        return try {
            val index = if (params is LoadParams.Append) params.key else FINNHUB_STARTING_INDEX
            val limit = params.loadSize
//            val r = Random.nextInt(10)
//            val picsumResponse = if (r < 3) {
//                listOf()
//            } else {
//            delay(2000) //fake loading time
            val finnhubResponse = FinnhubApi.finnhubApiRetrofitService.symbolList("US").await().subList(0, 10)
            finnhubResponse.forEach {
                it.quote = FinnhubApi.finnhubApiRetrofitService.symbolQuote(it.symbol).await()
            }
//            }
            val nextKey = if (finnhubResponse.isEmpty()) {
                null
            } else {
                index + (params.loadSize / 10)
            }

//            LoadResult.Page(
//                data = picsumResponse,
//                prevKey = if (page == FINNHUB_STARTING_INDEX) null else page - 1,
//                nextKey = nextKey
//            )
            LoadResult.Page(
                data = finnhubResponse,
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, FinnhubStockSymbol>): Int {
        return state.anchorPosition?: FINNHUB_STARTING_INDEX
    }
}