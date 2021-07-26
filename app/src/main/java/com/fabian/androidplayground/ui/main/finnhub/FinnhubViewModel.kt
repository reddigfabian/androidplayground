package com.fabian.androidplayground.ui.main.finnhub

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.ui.main.finnhub.detail.views.FinnhubDetailFragmentArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
@FlowPreview
class FinnhubViewModel : ViewModel() {

    val finnhubStateFlow = MutableStateFlow(FinnhubState(FinnhubState.ViewState.List))

    fun handleNavigationDestinationChange(destinationID: Int) {
        for (value in FinnhubState.ViewState.values()) {
            if (value.destinationId == destinationID) {
                finnhubStateFlow.value = FinnhubState(value)
                break
            }
        }
    }

    fun onListItemSelected(item : FinnhubStockSymbol) {
        finnhubStateFlow.value = FinnhubState(FinnhubState.ViewState.Detail, FinnhubDetailFragmentArgs(item).toBundle())
    }

    class FinnhubState(val viewState : ViewState,
                           val viewStateArgs : Bundle? = null,
                           val navOptions : NavOptions? = null,
                           val extras : FragmentNavigator.Extras? = null
    ) {
        enum class ViewState(val destinationId : Int) {
            List(R.id.FinnhubListFragment),
            Detail(R.id.FinnhubDetailFragment),
        }
    }
}