package com.fabian.androidplayground.ui.main.picsum.list.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.common.state.StateViewModel
import com.fabian.androidplayground.ui.main.picsum.detail.views.LoremPicsumDetailFragmentArgs
import com.fabian.androidplayground.ui.main.picsum.state.LoremPicsumState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest

@ExperimentalCoroutinesApi
@FlowPreview
class LoremPicsumListViewModel : StateViewModel<LoremPicsumState>(), ItemClickPagingAdapter.ItemClickListener<LoremPicsumItemViewModel> {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private val filterItems = MutableStateFlow(mutableListOf<Picsum>())
    val isEmptyLiveData = MutableLiveData(false)

    var pagingSource : PagingSource<Int, Picsum>? = null

    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        LoremPicsumPagingSource().also { pagingSource = it }
    }

    val clearStateFlow = MutableStateFlow(false)

    val pagingData = clearStateFlow.transformLatest {
        emit(PagingData.empty())
        emitAll(Pager(config = PagingConfig(PAGE_SIZE, PAGE_SIZE*3), pagingSourceFactory = pagingSourceFactory).flow)
    }
        .cachedIn(viewModelScope)
        .combine(filterItems) { pagingData, filteredItems ->
            pagingData.filter {
                !filteredItems.contains(it)
            }
        }
        .asLiveData()


    val swipeRefreshing = MutableLiveData(false)

    fun onSwipeRefresh() {
        swipeRefreshing.value = true
        filterItems.value.clear()
    }

    override fun onItemClick(item: LoremPicsumItemViewModel) {
        setState(LoremPicsumState(LoremPicsumState.Detail, LoremPicsumDetailFragmentArgs(item.pic).toBundle()))
    }

    override fun onItemLongClick(item: LoremPicsumItemViewModel) {
        val r = filterItems.value.toMutableList()
        r.add(item.pic)
        filterItems.value = r
    }
}