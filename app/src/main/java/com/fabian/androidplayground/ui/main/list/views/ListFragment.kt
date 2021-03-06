package com.fabian.androidplayground.ui.main.list.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentListBinding
import com.fabian.androidplayground.ui.main.list.MainListAdapter
import com.fabian.androidplayground.ui.main.list.viewmodels.ListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class ListFragment : BaseDataBindingFragment<FragmentListBinding>(R.layout.fragment_list) {

    private val listViewModel : ListViewModel by activityViewModels { ListViewModel.Factory() }
    private val mainListAdapter = MainListAdapter()

    override fun setDataBoundViewModels(binding: FragmentListBinding) {
        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.listViewModel = listViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainListAdapter.addItemClickListener(listViewModel)

        mainListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                listViewModel.isEmptyLiveData.value = mainListAdapter.itemCount == 0
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                listViewModel.isEmptyLiveData.value = mainListAdapter.itemCount == 0
            }
        })

        context?.let { nonNullContext ->
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(ContextCompat.getColor(nonNullContext, android.R.color.transparent))
            val dpToPx = UIUtils.dpToPx(nonNullContext, 2.0f)
            gradientDrawable.setSize(dpToPx, dpToPx)

            val dividerItemDecorationV = DividerItemDecoration(nonNullContext, DividerItemDecoration.VERTICAL)
            val dividerItemDecorationH = DividerItemDecoration(nonNullContext, DividerItemDecoration.HORIZONTAL)
            dividerItemDecorationV.setDrawable(gradientDrawable)
            dividerItemDecorationH.setDrawable(gradientDrawable)
            binding.mainListRecycler.addItemDecoration(dividerItemDecorationH)
            binding.mainListRecycler.addItemDecoration(dividerItemDecorationV)
        }

        val refreshStateAdapter = LoadStateAdapter(mainListAdapter)
        mainListAdapter.addLoadStateListener {
            val refresh = it.refresh
            if (listViewModel.swipeRefreshing.value != true) {
                refreshStateAdapter.loadState = refresh
            } else {
                if (refresh is LoadState.NotLoading) {
                    listViewModel.swipeRefreshing.value = false
                }
            }
            listViewModel.isEmptyLiveData.value = (refresh is LoadState.NotLoading && it.append.endOfPaginationReached && mainListAdapter.itemCount == 0)
        }
        val withLoadStateFooter = mainListAdapter.withLoadStateFooter(LoadStateAdapter(mainListAdapter))
        withLoadStateFooter.addAdapter(refreshStateAdapter)

        listViewModel.swipeRefreshing.observe(viewLifecycleOwner) {
            mainListAdapter.refresh()
        }

        binding.mainListRecycler.layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.mainListRecycler.adapter = withLoadStateFooter
        listViewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                mainListAdapter.submitData(pagingData)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuClear -> {
                listViewModel.pagingSourceFactory.invalidate()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}