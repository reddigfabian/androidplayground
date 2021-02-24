package com.fabian.androidplayground.api.picsum

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.delayEach
import kotlinx.coroutines.flow.delayFlow

const val PAGE_SIZE = 50

object LoremPicsumRepository {
    fun imageList(): Flow<PagingData<Picsum>> = Pager(PagingConfig(PAGE_SIZE, PAGE_SIZE * 3)) {
        LoremPicsumPagingSource()
    }.flow
}