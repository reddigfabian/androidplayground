package com.fabian.androidplayground.ui.main.picsumroom.list.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentLoremPicsumRoomListBinding
import com.fabian.androidplayground.db.lorempicsum.LoremPicsumDatabase
import com.fabian.androidplayground.ui.main.picsumroom.list.LoremPicsumRoomListAdapter
import com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels.LoremPicsumRoomListViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumRoomListFragment : BaseDataBindingFragment<FragmentLoremPicsumRoomListBinding>(R.layout.fragment_lorem_picsum_room_list) {

    private val loremPicsumRoomListViewModel: LoremPicsumRoomListViewModel by viewModels {
        LoremPicsumRoomListViewModel.Factory(LoremPicsumDatabase.getInstance(requireContext()))
    }
    private lateinit var loremPicsumListAdapter : LoremPicsumRoomListAdapter

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumRoomListBinding) {
        binding.loremPicsumListViewModel = loremPicsumRoomListViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupNavigation()
        setupRecycler()
    }

    private fun setupNavigation() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){ //This will collect when started and cancel the coroutine when stopped
                loremPicsumRoomListViewModel.navigationInstructions.collectLatest {
                    findNavController().executeNavInstructions(it)
                }
            }
        }
    }

    private fun setupRecycler() {
        loremPicsumListAdapter = LoremPicsumRoomListAdapter(viewLifecycleOwner, this)
        loremPicsumListAdapter.addItemClickListener(loremPicsumRoomListViewModel)

        context?.let { nonNullContext ->
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(
                ContextCompat.getColor(
                    nonNullContext,
                    android.R.color.transparent
                )
            )
            val dpToPx = UIUtils.dpToPx(nonNullContext, 2.0f)
            gradientDrawable.setSize(dpToPx, dpToPx)

            val dividerItemDecorationV =
                DividerItemDecoration(nonNullContext, DividerItemDecoration.VERTICAL)
            val dividerItemDecorationH =
                DividerItemDecoration(nonNullContext, DividerItemDecoration.HORIZONTAL)
            dividerItemDecorationV.setDrawable(gradientDrawable)
            dividerItemDecorationH.setDrawable(gradientDrawable)
            binding.mainListRecycler.addItemDecoration(dividerItemDecorationH)
            binding.mainListRecycler.addItemDecoration(dividerItemDecorationV)
        }

//        val refreshStateAdapter = LoadStateAdapter(loremPicsumListAdapter)
//        val withLoadStateFooter = loremPicsumListAdapter.withLoadStateFooter(LoadStateAdapter(loremPicsumListAdapter))
//        withLoadStateFooter.addAdapter(0, refreshStateAdapter)
//        loremPicsumListAdapter.addLoadStateListener {
//            val refresh = it.refresh
//            if (loremPicsumRoomListViewModel.swipeRefreshing.value != true) {
//                refreshStateAdapter.loadState = refresh
//            } else {
//                if (refresh is LoadState.NotLoading) {
//                    loremPicsumRoomListViewModel.swipeRefreshing.value = false
//                }
//            }
//        }

        loremPicsumRoomListViewModel.swipeRefreshing.observe(viewLifecycleOwner) {
            if (it) {
                loremPicsumListAdapter.refresh()
            }
        }

//        binding.mainListRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainListRecycler.layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.mainListRecycler.adapter = loremPicsumListAdapter

        lifecycleScope.launch(IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                loremPicsumRoomListViewModel.pagingData.collect {
                    loremPicsumListAdapter.submitData(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuRefresh -> {
                loremPicsumListAdapter.refresh()
            }
            R.id.menuClear -> {
                loremPicsumRoomListViewModel.clearStateFlow.value = !loremPicsumRoomListViewModel.clearStateFlow.value
            }
            R.id.menuInvalidateSource -> {
                loremPicsumRoomListViewModel.pagingSource?.invalidate()
            }
            R.id.menuInvalidateFactory -> {
                loremPicsumRoomListViewModel.pagingSourceFactory.invalidate()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}