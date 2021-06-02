package com.fabian.androidplayground.ui.main.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

private const val TAG = "ListViewModel"

@ExperimentalCoroutinesApi
@FlowPreview
class ListViewModel : ViewModel(), ItemClickPagingAdapter.ItemClickListener<Picsum> {

    companion object {
        private const val PAGE_SIZE = 50
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel() as T
        }
    }

    private val mutablePicsumClickLiveData = MutableLiveData<Picsum>()
    val picsumClickLiveData : LiveData<Picsum> = mutablePicsumClickLiveData

    val isEmpty = MutableStateFlow(false)
    val isEmptyLiveData = isEmpty.asLiveData()

    private val _pagingDataViewStates =
        Pager(PagingConfig(PAGE_SIZE, PAGE_SIZE * 3)) { LoremPicsumPagingSource() }.flow
            .cachedIn(viewModelScope)
            .combine(isEmpty) { pagingData, empty ->
                if (empty) {
                    pagingData
                } else {
                    pagingData
                }
            }
            .asLiveData()

    val pagingDataViewStates: LiveData<PagingData<Picsum>> = _pagingDataViewStates

    val swipeRefreshing = MutableLiveData(false)

    fun onSwipeRefresh() {
        swipeRefreshing.value = true
    }

    override fun onItemClick(item: Picsum) {
        mutablePicsumClickLiveData.value = item
    }

    override fun onItemLongClick(item: Picsum) {
        isEmpty.value = true
    }
}