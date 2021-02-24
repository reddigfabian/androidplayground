package com.fabian.androidplayground.ui.main.list.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fabian.androidplayground.api.picsum.LoremPicsumRepository
import com.fabian.androidplayground.api.picsum.Picsum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

private const val TAG = "ListViewModel"

@ExperimentalCoroutinesApi
@FlowPreview
class ListViewModel(private val loremPicsumRepository: LoremPicsumRepository) : ViewModel() {

    class Factory(private val loremPicsumRepository: LoremPicsumRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel(loremPicsumRepository) as T
        }
    }

    private val itemClickFlow = MutableLiveData<MutableSharedFlow<Picsum>>()
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

    fun getItemClickFlowLiveData() : LiveData<MutableSharedFlow<Picsum>> {
        return itemClickFlow
    }

    fun setItemClickFlow(flow : MutableSharedFlow<Picsum>) {
        itemClickFlow.value = flow
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
}