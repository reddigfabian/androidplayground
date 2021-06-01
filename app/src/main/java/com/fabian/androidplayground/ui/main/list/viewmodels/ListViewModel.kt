package com.fabian.androidplayground.ui.main.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

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

    val isEmpty = MutableLiveData(false)

    private val mutablePicsumClickLiveData = MutableLiveData<Picsum>()
    val picsumClickLiveData : LiveData<Picsum> = mutablePicsumClickLiveData

    private val _pagingDataViewStates =
        Pager(PagingConfig(PAGE_SIZE, PAGE_SIZE * 3)) { LoremPicsumPagingSource() }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
            .let { it as MutableLiveData<PagingData<Picsum>> }

    val pagingDataViewStates: LiveData<PagingData<Picsum>> = _pagingDataViewStates

    val swipeRefreshing = MutableLiveData(false)

    fun onSwipeRefresh() {
        swipeRefreshing.value = true
    }

    override fun onItemClick(item: Picsum) {
        mutablePicsumClickLiveData.value = item
    }

    override fun onItemLongClick(item: Picsum) {
        val t = pagingDataViewStates.value
        _pagingDataViewStates.value = t!!.filter {
            false //for testing purposes, just filter EVERYTHING to pretend like the user deleted the last item
        }
    }
}