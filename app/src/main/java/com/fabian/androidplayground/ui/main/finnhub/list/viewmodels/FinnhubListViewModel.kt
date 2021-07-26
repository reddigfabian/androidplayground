package com.fabian.androidplayground.ui.main.finnhub.list.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.finnhub.FinnhubApi
import com.fabian.androidplayground.api.finnhub.FinnhubPagingSource
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.api.finnhub.Subscribe
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.ui.main.finnhub.detail.views.FinnhubDetailFragmentArgs
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private const val TAG = "FinnhubListViewModel"

@ExperimentalCoroutinesApi
@FlowPreview
class FinnhubListViewModel : ViewModel(), ItemClickPagingAdapter.ItemClickListener<FinnhubItemViewModel> {

    companion object {
        private const val PAGE_SIZE = 10
    }

    val navigationInstructions = MutableSharedFlow<NavInstructions>()

    private val filterItems = MutableStateFlow(mutableListOf<FinnhubStockSymbol>())
    val isEmptyLiveData = MutableLiveData(false)

    var pagingSource : PagingSource<Int, FinnhubStockSymbol>? = null

    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        FinnhubPagingSource().also { pagingSource = it }
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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            FinnhubApi.finnhubApiScarletService.observeWebSocketEvent()
                .collect {
                    Log.d(TAG, "Socket event: $it")
                }
        }
    }

    fun onSwipeRefresh() {
        swipeRefreshing.value = true
        filterItems.value.clear()
    }

    override fun onItemClick(item: FinnhubItemViewModel) {
        viewModelScope.launch {
            navigationInstructions.emit(NavInstructions(R.id.FinnhubDetailFragment, FinnhubDetailFragmentArgs(item.finnhubStock).toBundle()))
        }
    }

    override fun onItemLongClick(item: FinnhubItemViewModel) {
        val r = filterItems.value.toMutableList()
        r.add(item.finnhubStock)
        filterItems.value = r
    }
}