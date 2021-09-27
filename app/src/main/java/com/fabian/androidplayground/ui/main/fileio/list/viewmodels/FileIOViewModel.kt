package com.fabian.androidplayground.ui.main.fileio.list.viewmodels

import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.recyclerview.views.ItemClickListener

class FileIOViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore), ItemClickListener<FileItemViewModel> {
    override val TAG = "FileIOViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FileIOViewModel(dataStore) as T
    }

    val empty = MutableLiveData(true)
    override fun onItemClick(item: FileItemViewModel, view: View) {
//        TODO("Not yet implemented")
    }

    override fun onItemLongClick(item: FileItemViewModel, view: View) {
//        TODO("Not yet implemented")
    }
}