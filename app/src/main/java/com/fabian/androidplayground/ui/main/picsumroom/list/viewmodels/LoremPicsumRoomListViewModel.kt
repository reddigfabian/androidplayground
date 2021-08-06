package com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.LoremPicsumRemoteMediator
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.common.recyclerview.ItemClickVMProviderPagingAdapter
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import com.fabian.androidplayground.ui.main.picsum.detail.views.LoremPicsumDetailFragmentArgs
import com.fabian.androidplayground.ui.main.picsumroom.detail.views.LoremPicsumRoomDetailFragmentArgs
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@FlowPreview
class LoremPicsumRoomListViewModel private constructor(private val db : LoremPicsumDatabase): ViewModel(),
    ItemClickVMProviderPagingAdapter.ItemClickListener<Picsum> {

    class Factory(private val db : LoremPicsumDatabase) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LoremPicsumRoomListViewModel(db) as T
    }

    companion object {
        private const val PAGE_SIZE = 30
    }

    val navigationInstructions = MutableSharedFlow<NavInstructions>()

    private val filterItems = MutableStateFlow(mutableListOf<Picsum>())
    val isEmptyLiveData = MutableLiveData(false)

    var pagingSource : PagingSource<Int, Picsum>? = null

    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        db.getPicsumDao().pagingSource()
    }

    val clearStateFlow = MutableStateFlow(false)

//    val pagingData = clearStateFlow.transformLatest {
//        emit(PagingData.empty())
//        emitAll(Pager(config = PagingConfig(PAGE_SIZE, PAGE_SIZE*3), pagingSourceFactory = pagingSourceFactory).flow)
//    }
//        .cachedIn(viewModelScope)
//        .combine(filterItems) { pagingData, filteredItems ->
//            pagingData.filter {
//                !filteredItems.contains(it)
//            }
//        }
//        .asLiveData()

    val pagingData = Pager(config = PagingConfig(PAGE_SIZE), pagingSourceFactory = pagingSourceFactory, remoteMediator = LoremPicsumRemoteMediator(db)).flow
        .cachedIn(viewModelScope)

    val swipeRefreshing = MutableLiveData(false)

    fun onSwipeRefresh() {
        swipeRefreshing.value = true
        filterItems.value.clear()
    }

    override fun onItemClick(item: Picsum) {
        viewModelScope.launch {
            navigationInstructions.emit(NavInstructions(R.id.LoremPicsumDetailFragment, LoremPicsumRoomDetailFragmentArgs(item).toBundle()))
        }
    }

    override fun onItemLongClick(item: Picsum) {
        viewModelScope.launch(IO) {
            db.getPicsumDao().delete(item)
            db.getRepoDao().delete(item.index)
        }
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}