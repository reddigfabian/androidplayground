package com.fabian.androidplayground.ui.main.finnhub.detail.views

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentFinnhubDetailBinding
import com.fabian.androidplayground.ui.main.finnhub.detail.viewmodels.FinnhubDetailViewModel

class FinnhubDetailFragment : BaseDataBindingFragment<FragmentFinnhubDetailBinding>(R.layout.fragment_finnhub_detail) {
    private val args : FinnhubDetailFragmentArgs by navArgs()
    private val finnhubDetailViewModel : FinnhubDetailViewModel by viewModels {
        FinnhubDetailViewModel.Factory(args.finnhubStock, requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentFinnhubDetailBinding) {
        binding.finnhubDetailViewModel = finnhubDetailViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return finnhubDetailViewModel
    }
}