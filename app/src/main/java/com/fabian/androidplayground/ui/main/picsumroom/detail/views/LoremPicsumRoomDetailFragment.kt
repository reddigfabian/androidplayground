
package com.fabian.androidplayground.ui.main.picsumroom.detail.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.databinding.FragmentLoremPicsumRoomDetailBinding
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import com.fabian.androidplayground.ui.main.picsumroom.detail.viewmodels.LoremPicsumRoomDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumRoomDetailFragment : BaseDataBindingFragment<FragmentLoremPicsumRoomDetailBinding>(R.layout.fragment_lorem_picsum_room_detail) {
    private val args : LoremPicsumRoomDetailFragmentArgs by navArgs()
    private val loremPicsumDetailViewModel : LoremPicsumRoomDetailViewModel by viewModels {
        LoremPicsumRoomDetailViewModel.Factory(args.picsum, LoremPicsumDatabase.getInstance(requireContext()))
    }

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumRoomDetailBinding) {
        binding.loremPicsumDetailViewModel = loremPicsumDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loremPicsumDetailViewModel.navigationInstructions.collectLatest {
                    when (it) {
                        is NavBackInstruction -> {
                            findNavController().popBackStack()
                        }
                        else -> {
                            TODO("Not yet implemented")
                        }
                    }
                }
            }
        }
    }
}