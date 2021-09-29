package com.fabian.androidplayground.ui.main.fileio.list.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.IntentUtils
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentFileioBinding
import com.fabian.androidplayground.ui.main.fileio.list.FileListAdapter
import com.fabian.androidplayground.ui.main.fileio.list.viewmodels.FileIOViewModel
import com.fabian.androidplayground.ui.main.fileio.list.viewmodels.FileItemViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch


@FlowPreview
@ExperimentalCoroutinesApi
class FileIOFragment : BaseDataBindingFragment<FragmentFileioBinding>(R.layout.fragment_fileio) {
    override val TAG = "FileIOFragment"

    private val fileIOViewModel : FileIOViewModel by viewModels {
        FileIOViewModel.Factory(requireContext().dataStore)
    }

    private val args by navArgs<FileIOFragmentArgs>()

    private lateinit var fileListAdapter: FileListAdapter

    override fun setDataBoundViewModels(binding: FragmentFileioBinding) {
        binding.fileIOViewModel = fileIOViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openFileButton.setOnClickListener {
            openDocumentContract.launch(arrayOf("*/*"))
        }
        setupRecycler(view)
        args.intentArgs?.let { nonNullIntentArgs ->
            var uriList : List<Uri>? = null
            var text : String? = null
            nonNullIntentArgs.extras?.let { nonNullExtras ->
                when (nonNullIntentArgs.action) {
                    Intent.ACTION_SEND -> {
                        if (nonNullIntentArgs.type == "text/plain") {
                            text = IntentUtils.getTextFromIntentBundle(nonNullExtras)
                        } else {
                            uriList = IntentUtils.getFileUriFromIntentBundle(nonNullExtras)
                        }
                    }
                    Intent.ACTION_SEND_MULTIPLE -> {
                        uriList = IntentUtils.getMultipleFileUrisFromIntentBundle(nonNullExtras)
                    }
                    else -> {
                        // Handle other intents, such as being started from the home screen
                    }
                }
            }

            uriList?.let { uris ->
                Log.d(TAG, "onViewCreated: ${uris.size} uris")
                sendFileItemViewModelsToAdapter(mapUrisToFileItemViewModels(uris.toList()))
            }
            text?.let { textToSend ->
                Log.d(TAG, "onViewCreated: textToSend = $textToSend")
                sendFileItemViewModelsToAdapter(listOf(mapTextToFileItemViewModel(textToSend)))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fileListAdapter.addItemClickListener(fileIOViewModel)
    }

    override fun onStop() {
        super.onStop()
        fileListAdapter.removeItemClickListener(fileIOViewModel)
    }

    private fun setupRecycler(view : View) {
        fileListAdapter = FileListAdapter()

        val refreshStateAdapter = LoadStateAdapter(fileListAdapter)
        val withLoadStateFooter = fileListAdapter.withLoadStateFooter(LoadStateAdapter(fileListAdapter))
        withLoadStateFooter.addAdapter(0, refreshStateAdapter)

        fileListAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.fileRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.fileRecyclerView.adapter = withLoadStateFooter
    }

    private val openDocumentContract = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        sendFileItemViewModelsToAdapter(mapUrisToFileItemViewModels(uris))
    }

    private fun mapTextToFileItemViewModel(text : String) : FileItemViewModel {
        return FileItemViewModel(text)
    }

    private fun mapUrisToFileItemViewModels(uris : List<Uri>): List<FileItemViewModel> {
        val items = mutableListOf<FileItemViewModel>()
        activity?.let { nonnullActivity ->
            uris.forEach { uri ->
                var fileType = "N/A"
                nonnullActivity.contentResolver.getType(uri)?.let { type ->
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(type)?.let { extension ->
                        fileType = extension
                    }
                }
                items.add(FileItemViewModel(uri, fileType, UIUtils.getDrawableForUri(nonnullActivity, uri)))
            }
        }
        return items
    }

    private fun sendFileItemViewModelsToAdapter(items : List<FileItemViewModel>) {
        fileIOViewModel.empty.postValue(items.isEmpty())
        viewLifecycleOwner.lifecycleScope.launch {
            fileListAdapter.submitData(
                PagingData.from(items)
            )
        }
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return fileIOViewModel
    }
}