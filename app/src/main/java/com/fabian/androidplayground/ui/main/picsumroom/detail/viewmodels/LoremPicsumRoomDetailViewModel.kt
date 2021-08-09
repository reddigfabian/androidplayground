package com.fabian.androidplayground.ui.main.picsumroom.detail.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoremPicsumRoomDetailViewModel private constructor(val picsum : Picsum, private val db : LoremPicsumDatabase) : ViewModel() {

    class Factory(private val picsum : Picsum, private val db : LoremPicsumDatabase) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LoremPicsumRoomDetailViewModel(picsum, db) as T
    }

    val navigationInstructions = MutableSharedFlow<NavInstructions>()

    init {
        viewModelScope.launch {
            db.getPicsumDao().getPicsumFlow(picsum.id).collect {
                if (it == null) {
                    navigationInstructions.emit(NavBackInstruction)
                }
            }
        }
    }

    fun onClick(view : View) {
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