
package com.fabian.androidplayground.ui.main.picsumroom.detail.views

import android.graphics.Color
import android.os.Bundle
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
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.databinding.FragmentLoremPicsumRoomDetailBinding
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import com.fabian.androidplayground.ui.main.picsumroom.detail.viewmodels.LoremPicsumRoomDetailViewModel
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.ViewTreeObserver.OnGlobalLayoutListener

import com.fabian.androidplayground.common.animation.SwingInterpolator
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore


private const val TAG = "LoremPicsumRoomDetailFr"

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumRoomDetailFragment : BaseDataBindingFragment<FragmentLoremPicsumRoomDetailBinding>(R.layout.fragment_lorem_picsum_room_detail) {
    private val args : LoremPicsumRoomDetailFragmentArgs by navArgs()
    private val loremPicsumDetailViewModel : LoremPicsumRoomDetailViewModel by viewModels {
        LoremPicsumRoomDetailViewModel.Factory(args.picsum, LoremPicsumDatabase.getInstance(requireContext()), requireContext().dataStore)
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
        super.onViewCreated(view, savedInstanceState)

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

    override fun getViewModel(): BaseFragmentViewModel {
        return loremPicsumDetailViewModel
    }
}