package com.fabian.androidplayground.ui.main.launch.views

import android.annotation.SuppressLint
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
import android.text.util.Linkify
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fabian.androidplayground.common.links.LinkMaker
import kotlinx.android.synthetic.main.fragment_launch.*

private const val TAG = "LaunchFragment"

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchFragment : BaseDataBindingFragment<FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchViewModel = launchViewModel
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){ //This will collect when started and cancel the coroutine when stopped
                launchViewModel.navigationInstructions.collectLatest {
                    findNavController().executeNavInstructions(it)
                }
            }
        }

        val spannableText = (binding.linkTextView.text as? Spannable) ?: SpannableString(binding.linkTextView.text)
        Linkify.addLinks(spannableText, Linkify.ALL)
        try {
            val spans = spannableText.getSpans(0, spannableText.length, URLSpan::class.java)
            for (span in spans) {
                val start = spannableText.getSpanStart(span)
                val end = spannableText.getSpanEnd(span)
                spannableText.removeSpan(span)
                spannableText.setSpan(ConfirmURLSpan(span.url), start, end, 0)
            }
            binding.linkTextView.text = spannableText
        } catch (ex : Exception) {
            Log.e(TAG, "String : ${binding.linkTextView.text}", ex)
        }

        binding.linkTextView.setOnTouchListener { v, event ->
            (v as? TextView)?.let { textView ->

                val action = event.action

                if (action == MotionEvent.ACTION_UP || action  == MotionEvent.ACTION_DOWN) {
                    var x = event.x
                    var y = event.y
                    x -= textView.totalPaddingLeft
                    y -= textView.totalPaddingTop

                    x += textView.scrollX
                    y += textView.scrollY

                    val layout = textView.layout
                    val lineForVertical = layout.getLineForVertical(y.toInt())
                    val offsetForHorizontal = layout.getOffsetForHorizontal(lineForVertical, x)

                    val text = textView.text
                    val buffer = (textView.text as? Spannable) ?: SpannableString(text)
                    val links = buffer.getSpans(
                        offsetForHorizontal,
                        offsetForHorizontal,
                        URLSpan::class.java
                    )
                    if (links.isNotEmpty()) {
                        if (action == MotionEvent.ACTION_UP) {
                            links[0].onClick(textView)
                        }
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } ?: kotlin.run {
                false
            }
        }
        binding.clickableLayout.visibility = View.VISIBLE

    }

    class ConfirmURLSpan(url : String) : URLSpan(url) {
        override fun onClick(view: View) {
            AlertDialog.Builder(view.context)
                .setMessage("Yo dawg, you sure?")
                .setPositiveButton("I like danger") { _, _ ->
                    super.onClick(view)
                }
                .show()
        }
    }

}