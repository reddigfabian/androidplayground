package com.fabian.androidplayground.ui.main.picsumroom.list.views

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.databinding.FragmentLoremPicsumRoomListBinding
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import com.fabian.androidplayground.ui.main.picsumroom.list.LoremPicsumRoomListAdapter
import com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels.LoremPicsumRoomListViewModel
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumRoomListFragment : BaseDataBindingFragment<FragmentLoremPicsumRoomListBinding>(R.layout.fragment_lorem_picsum_room_list) {

    private val loremPicsumRoomListViewModel: LoremPicsumRoomListViewModel by viewModels {
        LoremPicsumRoomListViewModel.Factory(LoremPicsumDatabase.getInstance(requireContext()), requireContext().dataStore)
    }
    private lateinit var loremPicsumListAdapter : LoremPicsumRoomListAdapter

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumRoomListBinding) {
        binding.loremPicsumListViewModel = loremPicsumRoomListViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        setupRecycler(view)
    }

    override fun onStart() {
        super.onStart()
        loremPicsumListAdapter.addItemClickListener(loremPicsumRoomListViewModel)
    }

    override fun onStop() {
        super.onStop()
        loremPicsumListAdapter.removeItemClickListener(loremPicsumRoomListViewModel)
    }

    private fun setupRecycler(view : View) {
        loremPicsumListAdapter = LoremPicsumRoomListAdapter()

        val refreshStateAdapter = LoadStateAdapter(loremPicsumListAdapter)
        val withLoadStateFooter = loremPicsumListAdapter.withLoadStateFooter(LoadStateAdapter(loremPicsumListAdapter))
        withLoadStateFooter.addAdapter(0, refreshStateAdapter)
        loremPicsumListAdapter.addLoadStateListener {
            val refresh = it.refresh
            if (loremPicsumRoomListViewModel.swipeRefreshing.value != true) {
                refreshStateAdapter.loadState = refresh
            } else {
                if (refresh is LoadState.NotLoading) {
                    loremPicsumRoomListViewModel.swipeRefreshing.value = false
                }
            }

        }

        loremPicsumRoomListViewModel.swipeRefreshing.observe(viewLifecycleOwner) {
            if (it) {
                loremPicsumListAdapter.refresh()
            }
        }

        loremPicsumListAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

//        binding.mainListRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainListRecycler.layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.mainListRecycler.adapter = withLoadStateFooter

        loremPicsumListAdapter.addOnPagesUpdatedListener {
            view.doOnPreDraw { startPostponedEnterTransition() }
        }

        viewLifecycleOwner.lifecycleScope.launch(IO) {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loremPicsumRoomListViewModel.pagingData.collectLatest {
                    loremPicsumListAdapter.submitData(it)
                }
            }
        }
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return loremPicsumRoomListViewModel
    }
}