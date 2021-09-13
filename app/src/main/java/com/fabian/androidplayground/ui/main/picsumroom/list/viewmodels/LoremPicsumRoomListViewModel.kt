package com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.UncheckedIOException

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

    val isEmptyLiveData = MutableLiveData(false)

    private val pagingSourceFactory = InvalidatingPagingSourceFactory {
        db.getPicsumDao().pagingSource()
    }

    val pagingData = Pager(config = PagingConfig(PAGE_SIZE), pagingSourceFactory = pagingSourceFactory, remoteMediator = LoremPicsumRemoteMediator(db)).flow
        .cachedIn(viewModelScope)

    val swipeRefreshing = MutableLiveData(false)

    fun onSwipeRefresh() {
        swipeRefreshing.value = true
    }

    override fun onItemClick(item: Picsum, view: View) {
        viewModelScope.launch {
            navigationInstructions.emit(NavInstructions(navDestinationID = R.id.LoremPicsumDetailFragment, navArgs = LoremPicsumRoomDetailFragmentArgs(item).toBundle(), navigatorExtras = FragmentNavigatorExtras(view to "listToDetailImageCard")))
        }
    }

    override fun onItemLongClick(item: Picsum, view: View) {
        viewModelScope.launch(IO) {
            db.withTransaction {
                db.getPicsumDao().delete(item)
                db.getRemoteKeysDao().delete(item.id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}