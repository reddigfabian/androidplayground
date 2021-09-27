package com.fabian.androidplayground.ui.main.picsum.list.viewmodels

import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import androidx.paging.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.LoremPicsumPagingSource
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.recyclerview.views.ItemClickListener
import com.fabian.androidplayground.ui.main.coroutines.viewmodels.CoroutinesViewModel
import com.fabian.androidplayground.ui.main.picsum.detail.views.LoremPicsumDetailFragmentArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class LoremPicsumListViewModel private constructor(dataStore: DataStore<Preferences>) : BaseFragmentViewModel(dataStore), ItemClickListener<LoremPicsumItemViewModel> {

    companion object {
        private const val PAGE_SIZE = 30
    }

    override val TAG = "LoremPicsumListViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LoremPicsumListViewModel(dataStore) as T
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

    override fun onItemClick(item: LoremPicsumItemViewModel, view : View) {
        viewModelScope.launch {
            navigationInstructions.emit(NavToInstructions(R.id.LoremPicsumDetailFragment, LoremPicsumDetailFragmentArgs(item.picsum).toBundle()))
        }
    }

    override fun onItemLongClick(item: LoremPicsumItemViewModel, view : View) {
        val r = filterItems.value.toMutableList()
        r.add(item.picsum)
        filterItems.value = r
    }
}