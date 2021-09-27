package com.fabian.androidplayground.ui.main.launch.viewmodels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.NavToInstructions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "LaunchViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LaunchViewModel(dataStore) as T
    }

    val greetingString = MutableLiveData<String?>()

    private var job : Job? = null

    init {
        job = viewModelScope.launch {
            getNameFlow().collect {
                greetingString.postValue("Greetings, ${it?:"UNKNOWN"}")
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
    }

    fun onClick(view : View) {
        viewModelScope.launch {
            when (view.id) {
                R.id.picsumButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.lorem_picsum_nav_graph))
                }
                R.id.picsumRoomButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.lorem_picsum_room_nav_graph))
                }
                R.id.finnhubButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.finnhub_nav_graph))
                }
                R.id.tensorFlowButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.tensorflow_nav_graph))
                }
                R.id.coroutinesButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.coroutines_nav_graph))
                }
                R.id.flowsButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.flows_nav_graph))
                }
                R.id.fileIOButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.fileio_nav_graph))
                }
                R.id.changePasswordButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.choose_password_nav_graph))
                }
                R.id.changeNameButton -> {
                    navigationInstructions.emit(NavToInstructions(R.id.choose_name_nav_graph))
                }
                R.id.clickableLayout -> {
                    Toast.makeText(view.context, "Clicked on layout", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}