package com.fabian.androidplayground.ui.main.finnhub.detail.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentFinnhubDetailBinding
import com.fabian.androidplayground.databinding.FragmentLoremPicsumDetailBinding
import com.fabian.androidplayground.ui.main.picsum.detail.viewmodels.FinnhubDetailViewModel

class FinnhubDetailFragment : BaseDataBindingFragment<FragmentFinnhubDetailBinding>(R.layout.fragment_finnhub_detail) {
    private val finnhubDetailViewModel : FinnhubDetailViewModel by viewModels()
    private val args : FinnhubDetailFragmentArgs by navArgs()

    override fun setDataBoundViewModels(binding: FragmentFinnhubDetailBinding) {
        binding.finnhubDetailViewModel = finnhubDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        finnhubDetailViewModel.pic = args.picsum
    }
}