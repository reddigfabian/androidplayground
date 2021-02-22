package com.fabian.androidplayground.ui.main.list.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fabian.androidplayground.api.picsum.LoremPicsumRepository
import com.fabian.androidplayground.api.picsum.Picsum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@FlowPreview
class ListViewModel(private val loremPicsumRepository: LoremPicsumRepository) : ViewModel() {
    class Factory(private val loremPicsumRepository: LoremPicsumRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListViewModel(loremPicsumRepository) as T
        }
    }

    private val selectedChannel = Channel<Pair<View, Picsum>>(Channel.CONFLATED)

    val swipeRefreshing = MutableLiveData(false)

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
    private val refreshCh = Channel<Unit>(Channel.CONFLATED)

    val images = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty() },
        refreshCh.receiveAsFlow().flatMapLatest { loremPicsumRepository.imageList() }
    ).flattenMerge(2).cachedIn(viewModelScope)

    init {
        refresh(false)
    }

    fun onSwipeRefresh() {
        refresh(true)
    }

    private fun refresh(isFromSwipe : Boolean) {
        swipeRefreshing.value = isFromSwipe
        if (isFromSwipe) {
            clearListCh.offer(Unit)
        }
        refreshCh.offer(Unit)
    }

    fun getItemSelectedFlow() : Flow<Pair<View, Picsum>> {
        return selectedChannel.receiveAsFlow()
    }

    fun onItemClick(item: Pair<View, Picsum>) {
        selectedChannel.offer(item)
    }
}