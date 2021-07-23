package com.fabian.androidplayground.ui.main.finnhub.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest

@ExperimentalCoroutinesApi
@FlowPreview
class FinnhubListViewModel : ViewModel(), ItemClickPagingAdapter.ItemClickListener<FinnhubItemViewModel> {

    companion object {
        private const val PAGE_SIZE = 10
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FinnhubListViewModel() as T
        }
    }

    private val filterItems = MutableStateFlow(mutableListOf<Picsum>())
    val isEmptyLiveData = MutableLiveData(false)

    private val mutablePicsumClickLiveData = MutableLiveData<Picsum>()
    val picsumClickLiveData : LiveData<Picsum> = mutablePicsumClickLiveData

    var pagingSource : PagingSource<Int, Picsum>? = null

    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        LoremPicsumPagingSource().also { pagingSource = it }
    }

    val clearStateFlow = MutableStateFlow(false)

    val pagingData = clearStateFlow.transformLatest {
        emit(PagingData.empty())
        emitAll(Pager(config = PagingConfig(PAGE_SIZE, PAGE_SIZE *3), pagingSourceFactory = pagingSourceFactory).flow)
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

    override fun onItemClick(item: FinnhubItemViewModel) {
        mutablePicsumClickLiveData.value = item.pic
    }

    override fun onItemLongClick(item: FinnhubItemViewModel) {
        val r = filterItems.value.toMutableList()
        r.add(item.pic)
        filterItems.value = r
    }
}