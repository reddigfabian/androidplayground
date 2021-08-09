package com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import androidx.room.withTransaction
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.LoremPicsumRemoteMediator
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
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
    ItemClickPagingAdapter.ItemClickListener<Picsum> {

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

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        db.getPicsumDao().pagingSource()
    }

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

    override fun onItemLongClick(pic: Picsum) {
        viewModelScope.launch(IO) {
            db.withTransaction {
                db.getPicsumDao().delete(pic)
                db.getRemoteKeysDao().delete(pic.id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}