package com.fabian.androidplayground.ui.main.finnhub.list.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.finnhub.FinnhubApi
import com.fabian.androidplayground.api.finnhub.FinnhubPagingSource
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.recyclerview.views.ItemClickListener
import com.fabian.androidplayground.ui.main.finnhub.detail.views.FinnhubDetailFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "FinnhubListViewModel"

@ExperimentalCoroutinesApi
@FlowPreview
class FinnhubListViewModel : ViewModel(), ItemClickListener<FinnhubItemViewModel> {

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

    override fun onItemClick(item: FinnhubItemViewModel, view : View) {
        viewModelScope.launch {
            navigationInstructions.emit(NavInstructions(R.id.FinnhubDetailFragment, FinnhubDetailFragmentArgs(item.finnhubStock).toBundle()))
        }
    }

    override fun onItemLongClick(item: FinnhubItemViewModel, view : View) {
        val r = filterItems.value.toMutableList()
        r.add(item.finnhubStock)
        filterItems.value = r
    }
}