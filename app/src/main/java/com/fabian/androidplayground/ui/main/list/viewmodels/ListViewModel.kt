package com.fabian.androidplayground.ui.main.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.ui.main.list.MainListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

private const val TAG = "ListViewModel"

@ExperimentalCoroutinesApi
@FlowPreview
class ListViewModel : ViewModel(), ItemClickPagingAdapter.ItemClickListener<Picsum> {

    companion object {
        private const val PAGE_SIZE = 1
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel() as T
        }
    }

    private val filterItems = MutableStateFlow(mutableListOf<Picsum>())
    val isEmptyLiveData = MutableLiveData(true)

    private val mutablePicsumClickLiveData = MutableLiveData<Picsum>()
    val picsumClickLiveData : LiveData<Picsum> = mutablePicsumClickLiveData

    private val _pagingDataViewStates =
        Pager(PagingConfig(PAGE_SIZE, 0)) { LoremPicsumPagingSource() }.flow
            .cachedIn(viewModelScope)
            .combine(filterItems) { pagingData, filteredItems ->
                pagingData.filter {
                    !filteredItems.contains(it)
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
        val r = filterItems.value.toMutableList()
        r.add(item)
        filterItems.value = r
    }
}