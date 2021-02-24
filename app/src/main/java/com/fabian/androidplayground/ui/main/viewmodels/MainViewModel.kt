package com.fabian.androidplayground.ui.main.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.ui.main.detail.views.DetailFragmentArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow

private const val TAG = "MainViewModel"

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel : ViewModel() {

    private val titleResID = MutableLiveData<Int>()
    private val mainStatePublisher = Channel<MainState>(Channel.CONFLATED)
    private lateinit var mainState : MainState


    fun mainStateAsFlow() : Flow<MainState> {
        return mainStatePublisher.receiveAsFlow().distinctUntilChanged()
    }

    fun getTitleResID() : LiveData<Int> {
        return titleResID
    }

    private fun setMainState(newState : MainState, publish : Boolean) {
        mainState = newState
        if (publish) {
            mainStatePublisher.offer(mainState)
        }
        titleResID.value = mainState.viewState.titleResID
    }

    fun handleNavigationDestinationChange(destinationID: Int) {
        for (value in MainState.ViewState.values()) {
            if (value.destinationId == destinationID) {
                setMainState(MainState(value), false)
                break
            }
        }
    }

    fun onClick(viewID : Int) {
        when (viewID) {
            R.id.startButton -> {
                setMainState(MainState(MainState.ViewState.List), true)
            }
        }
    }

    fun onListItemSelected(item : Picsum) {
        setMainState(MainState(MainState.ViewState.Detail, DetailFragmentArgs(item).toBundle()), true)
    }

    class MainState(val viewState : ViewState,
                    val viewStateArgs : Bundle? = null,
                    val navOptions : NavOptions? = null,
                    val extras : FragmentNavigator.Extras? = null
    ) {
        enum class ViewState(val titleResID : Int, val destinationId : Int) {
            Launch(R.string.launch, R.id.MainLaunchFragment),
            List(R.string.list, R.id.MainListFragment),
            Detail(R.string.detail, R.id.MainDetailFragment),
        }
    }
}