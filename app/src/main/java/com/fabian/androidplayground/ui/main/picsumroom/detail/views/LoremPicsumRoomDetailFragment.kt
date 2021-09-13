
package com.fabian.androidplayground.ui.main.picsumroom.detail.views

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentLoremPicsumRoomDetailBinding
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import com.fabian.androidplayground.ui.main.picsumroom.detail.viewmodels.LoremPicsumRoomDetailViewModel
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.android.synthetic.main.fragment_lorem_picsum_room_detail.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.ViewTreeObserver.OnGlobalLayoutListener

import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.OvershootInterpolator
import com.fabian.androidplayground.common.animation.SwingInterpolator
import kotlin.math.PI


private const val TAG = "LoremPicsumRoomDetailFr"

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumRoomDetailFragment : BaseDataBindingFragment<FragmentLoremPicsumRoomDetailBinding>(R.layout.fragment_lorem_picsum_room_detail) {
    private val args : LoremPicsumRoomDetailFragmentArgs by navArgs()
    private val loremPicsumDetailViewModel : LoremPicsumRoomDetailViewModel by viewModels {
        LoremPicsumRoomDetailViewModel.Factory(args.picsum, LoremPicsumDatabase.getInstance(requireContext()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        exitTransition = MaterialFadeThrough()
//        enterTransition = MaterialFadeThrough().apply {
//            duration = 600
//        }

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            scrimColor = Color.TRANSPARENT
            addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {
//                    TODO("Not yet implemented")
                }

                override fun onTransitionEnd(transition: Transition) {
                    binding.detailAuthorCard.visibility = View.VISIBLE
                    binding.detailAuthorCard.animate()
                        .rotationX(0.0f)
                        .setInterpolator(SwingInterpolator())
                        .setDuration(6000)

                }

                override fun onTransitionCancel(transition: Transition) {
//                    TODO("Not yet implemented")
                }

                override fun onTransitionPause(transition: Transition) {
//                    TODO("Not yet implemented")
                }

                override fun onTransitionResume(transition: Transition) {
//                    TODO("Not yet implemented")
                }

            })
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumRoomDetailBinding) {
        binding.loremPicsumDetailViewModel = loremPicsumDetailViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loremPicsumDetailViewModel.navigationInstructions.collectLatest {
                    when (it) {
                        is NavBackInstruction -> {
                            findNavController().popBackStack()
                        }
                        else -> {
                            //TODO("Not yet implemented")
                        }
                    }
                }
            }
        }

        val viewTreeObserver = binding.detailAuthorCard.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.detailAuthorCard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    binding.detailAuthorCard.pivotX = binding.detailAuthorCard.width/2.0f
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.picsum_room_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeletePicsum -> {
                loremPicsumDetailViewModel.deletePicsum()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}