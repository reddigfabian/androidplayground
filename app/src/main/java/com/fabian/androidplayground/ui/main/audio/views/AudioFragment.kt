package com.fabian.androidplayground.ui.main.audio.views

import androidx.fragment.app.activityViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentAudioBinding
import com.fabian.androidplayground.ui.main.audio.viewmodels.AudioViewModel

class AudioFragment : BaseDataBindingFragment<FragmentAudioBinding>(R.layout.fragment_audio) {
    private val audioViewModel: AudioViewModel by activityViewModels()

    override fun setDataBoundViewModels(binding: FragmentAudioBinding) {
        binding.audioViewModel = audioViewModel
    }
}