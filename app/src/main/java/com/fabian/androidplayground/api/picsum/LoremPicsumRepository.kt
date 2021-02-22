package com.fabian.androidplayground.api.picsum

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 50

class LoremPicsumRepository {
    fun imageList(): Flow<PagingData<Picsum>> = Pager(PagingConfig(PAGE_SIZE, PAGE_SIZE *2)) {
        LoremPicsumPagingSource()
    }.flow
}