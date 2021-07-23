package com.fabian.androidplayground.ui.main.finnhub.list.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentFinnhubListBinding
import com.fabian.androidplayground.ui.main.finnhub.list.FinnhubListAdapter
import com.fabian.androidplayground.ui.main.finnhub.list.viewmodels.FinnhubListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class FinnhubListFragment : BaseDataBindingFragment<FragmentFinnhubListBinding>(R.layout.fragment_finnhub_list) {

    private val finnhubListViewModel: FinnhubListViewModel by navGraphViewModels(R.navigation.main_nav_graph)
    private lateinit var mainListAdapter: FinnhubListAdapter

    override fun setDataBoundViewModels(binding: FragmentFinnhubListBinding) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.listViewModel = finnhubListViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        mainListAdapter = FinnhubListAdapter(viewLifecycleOwner)
        mainListAdapter.addItemClickListener(finnhubListViewModel)

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

        val refreshStateAdapter = LoadStateAdapter(mainListAdapter)
        val withLoadStateFooter = mainListAdapter.withLoadStateFooter(LoadStateAdapter(mainListAdapter))
        withLoadStateFooter.addAdapter(0, refreshStateAdapter)
        mainListAdapter.addLoadStateListener {
            val refresh = it.refresh
            if (finnhubListViewModel.swipeRefreshing.value != true) {
                refreshStateAdapter.loadState = refresh
            } else {
                if (refresh is LoadState.NotLoading) {
                    finnhubListViewModel.swipeRefreshing.value = false
                }
            }
        }

        finnhubListViewModel.swipeRefreshing.observe(viewLifecycleOwner) {
            mainListAdapter.refresh()
        }

        binding.mainListRecycler.layoutManager =
            StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.mainListRecycler.adapter = withLoadStateFooter
        finnhubListViewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            mainListAdapter.setData(pagingData)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuClear -> {
                finnhubListViewModel.clearStateFlow.value = !finnhubListViewModel.clearStateFlow.value
            }
            R.id.menuInvalidateSource -> {
                finnhubListViewModel.pagingSource?.invalidate()
            }
            R.id.menuInvalidateFactory -> {
                finnhubListViewModel.pagingSourceFactory.invalidate()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}