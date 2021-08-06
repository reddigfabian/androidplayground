package com.fabian.androidplayground.ui.main.launch.viewmodels

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.NavInstructions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchViewModel : ViewModel() {

    val navigationInstructions = MutableSharedFlow<NavInstructions>()

    fun onClick(view : View) {
        viewModelScope.launch {
            when (view.id) {
                R.id.picsumRoomButton -> {
                    navigationInstructions.emit(NavInstructions(R.id.lorem_picsum_room_nav_graph))
                }
            }
        }
    }
}