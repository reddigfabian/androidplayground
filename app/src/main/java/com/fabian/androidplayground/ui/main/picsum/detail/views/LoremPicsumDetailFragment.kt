package com.fabian.androidplayground.ui.main.picsum.detail.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentLoremPicsumDetailBinding
import com.fabian.androidplayground.ui.main.picsum.detail.viewmodels.LoremPicsumDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumDetailFragment : BaseDataBindingFragment<FragmentLoremPicsumDetailBinding>(R.layout.fragment_lorem_picsum_detail) {
    private val loremPicsumDetailViewModel : LoremPicsumDetailViewModel by viewModels {
        LoremPicsumDetailViewModel.Factory(requireContext().dataStore)
    }
    private val args : LoremPicsumDetailFragmentArgs by navArgs()

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumDetailBinding) {
        binding.loremPicsumDetailViewModel = loremPicsumDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loremPicsumDetailViewModel.pic = args.picsum
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return loremPicsumDetailViewModel
    }
}