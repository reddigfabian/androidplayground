package com.fabian.androidplayground.ui.main.picsum.detail.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.databinding.FragmentLoremPicsumDetailBinding
import com.fabian.androidplayground.ui.main.picsum.detail.viewmodels.LoremPicsumDetailViewModel
import com.fabian.androidplayground.ui.main.picsum.views.AbstractLoremPicsumFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumDetailFragment : AbstractLoremPicsumFragment<FragmentLoremPicsumDetailBinding>(R.layout.fragment_lorem_picsum_detail) {
    private val loremPicsumDetailViewModel : LoremPicsumDetailViewModel by viewModels()
    private val args : LoremPicsumDetailFragmentArgs by navArgs()

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumDetailBinding) {
        binding.loremPicsumDetailViewModel = loremPicsumDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loremPicsumDetailViewModel.pic = args.picsum
    }
}