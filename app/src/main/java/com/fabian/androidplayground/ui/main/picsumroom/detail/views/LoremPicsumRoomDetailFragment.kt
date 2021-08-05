package com.fabian.androidplayground.ui.main.picsumroom.detail.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentLoremPicsumDetailBinding
import com.fabian.androidplayground.ui.main.picsum.detail.viewmodels.LoremPicsumDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumRoomDetailFragment : BaseDataBindingFragment<FragmentLoremPicsumDetailBinding>(R.layout.fragment_lorem_picsum_detail) {
    private val loremPicsumDetailViewModel : LoremPicsumDetailViewModel by viewModels()
    private val args : LoremPicsumRoomDetailFragmentArgs by navArgs()

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumDetailBinding) {
        binding.loremPicsumDetailViewModel = loremPicsumDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loremPicsumDetailViewModel.pic = args.picsum
    }
}