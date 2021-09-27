package com.fabian.androidplayground.ui.main.picsumroom.detail.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoremPicsumRoomDetailViewModel private constructor(
    val picsum: Picsum,
    private val db: LoremPicsumDatabase,
    dataStore: DataStore<Preferences>
) : BaseFragmentViewModel(dataStore) {

    override val TAG = "LoremPicsumRoomDetailViewModel"

    class Factory(
        private val picsum: Picsum,
        private val db: LoremPicsumDatabase,
        private val dataStore: DataStore<Preferences>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LoremPicsumRoomDetailViewModel(picsum, db, dataStore) as T
    }

    init {
        viewModelScope.launch {
            db.getPicsumDao().getPicsumFlow(picsum.id).collect {
                if (it == null) {
                    navigationInstructions.emit(NavBackInstruction)
                }
            }
        }
    }

    fun deletePicsum() {
        picsum.let { pic ->
            viewModelScope.launch(IO) {
                db.withTransaction {
                    db.getPicsumDao().delete(pic)
                    db.getRemoteKeysDao().delete(pic.id)
                }
            }
        }
    }
}