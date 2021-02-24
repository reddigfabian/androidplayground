package com.fabian.androidplayground.ui.main.detail.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentDetailBinding
import com.fabian.androidplayground.ui.main.detail.viewmodels.DetailViewModel
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel

class DetailFragment : BaseDataBindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {
    private val detailViewModel : DetailViewModel by activityViewModels()
    private val args : DetailFragmentArgs by navArgs()

    override fun setDataBoundViewModels(binding: FragmentDetailBinding) {
        binding.detailViewModel = detailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailViewModel.pic = args.picsum
    }
}