package com.fabian.androidplayground.ui.main.launch.views

import android.app.AlertDialog
import android.os.Bundle
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import android.text.method.SingleLineTransformationMethod
import android.util.Log
import androidx.core.content.ContextCompat
import com.fabian.androidplayground.common.links.LinkMaker

private const val TAG = "LaunchFragment"

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchFragment : BaseDataBindingFragment<FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchViewModel = launchViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){ //This will collect when started and cancel the coroutine when stopped
                launchViewModel.navigationInstructions.collectLatest {
                    findNavController().executeNavInstructions(it)
                }
            }
        }

//        binding.linkTextView.highlightColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
//        LinkMaker.addLinks(binding.linkTextView, LinkMaker.ALL)
//        (binding.linkTextView.text as? SpannableString)?.let { spannable ->
//            val urlSpans = mutableMapOf<URLSpan, IntRange>()
//            spannable.getSpans(0, spannable.length, URLSpan::class.java).forEach {
//                urlSpans[it] = spannable.getSpanStart(it) .. spannable.getSpanEnd(it)
//            }
//            Log.d(TAG, "$urlSpans")
//
//            var start = 0
//            urlSpans.forEach {
//                spannable.removeSpan(it.key)
//                spannable.setSpan(ClickPassthroughSpan(), start, it.value.first, 0)
//                spannable.setSpan(ConfirmURLSpan(it.key.url), it.value.first, it.value.last, 0)
//                start = it.value.last
//            }
//            binding.linkTextView.text = spannable
//        }
//        binding.linkTextView.transformationMethod = SingleLineTransformationMethod()
    }

//    class ClickPassthroughSpan : ClickableSpan() {
//        override fun onClick(view: View) {
//            (view.parent as View).performClick()
//        }
//
//        override fun updateDrawState(ds: TextPaint) {
//            ds.color = ds.color
//            ds.isUnderlineText = false
//        }
//    }
//
//    class ConfirmURLSpan(url : String) : URLSpan(url) {
//        override fun onClick(view: View) {
//            AlertDialog.Builder(view.context)
//                .setMessage("Yo dawg, you sure?")
//                .setPositiveButton("I like danger") { _, _ ->
//                    super.onClick(view)
//                }
//                .show()
//        }
//    }

}