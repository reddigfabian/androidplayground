package com.fabian.androidplayground.ui.main.picsum.list.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentLoremPicsumListBinding
import com.fabian.androidplayground.ui.main.picsum.list.LoremPicsumListAdapter
import com.fabian.androidplayground.ui.main.picsum.list.viewmodels.LoremPicsumListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LoremPicsumListFragment : BaseDataBindingFragment<FragmentLoremPicsumListBinding>(R.layout.fragment_lorem_picsum_list) {

    private val loremPicsumListViewModel: LoremPicsumListViewModel by viewModels {
        LoremPicsumListViewModel.Factory(requireContext().dataStore)
    }
    private lateinit var loremPicsumListAdapter : LoremPicsumListAdapter

    override fun setDataBoundViewModels(binding: FragmentLoremPicsumListBinding) {
        binding.loremPicsumListViewModel = loremPicsumListViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecycler()
    }

    private fun setupRecycler() {
        loremPicsumListAdapter = LoremPicsumListAdapter(viewLifecycleOwner, this)
        loremPicsumListAdapter.addItemClickListener(loremPicsumListViewModel)
        viewLifecycleOwner.lifecycle.addObserver(loremPicsumListAdapter)

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

        val refreshStateAdapter = LoadStateAdapter(loremPicsumListAdapter)
        val withLoadStateFooter = loremPicsumListAdapter.withLoadStateFooter(LoadStateAdapter(loremPicsumListAdapter))
        withLoadStateFooter.addAdapter(0, refreshStateAdapter)
        loremPicsumListAdapter.addLoadStateListener {
            val refresh = it.refresh
            if (loremPicsumListViewModel.swipeRefreshing.value != true) {
                refreshStateAdapter.loadState = refresh
            } else {
                if (refresh is LoadState.NotLoading) {
                    loremPicsumListViewModel.swipeRefreshing.value = false
                }
            }
        }

        loremPicsumListViewModel.swipeRefreshing.observe(viewLifecycleOwner) {
            loremPicsumListAdapter.refresh()
        }

//        binding.mainListRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainListRecycler.layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.mainListRecycler.adapter = withLoadStateFooter
        loremPicsumListViewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            loremPicsumListAdapter.setData(pagingData)
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
                loremPicsumListViewModel.clearStateFlow.value = !loremPicsumListViewModel.clearStateFlow.value
            }
            R.id.menuInvalidateSource -> {
                loremPicsumListViewModel.pagingSource?.invalidate()
            }
            R.id.menuInvalidateFactory -> {
                loremPicsumListViewModel.pagingSourceFactory.invalidate()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return loremPicsumListViewModel
    }
}