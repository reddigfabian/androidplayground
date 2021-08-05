package com.fabian.androidplayground.ui.main.finnhub.list.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentFinnhubListBinding
import com.fabian.androidplayground.ui.main.finnhub.list.FinnhubListAdapter
import com.fabian.androidplayground.ui.main.finnhub.list.viewmodels.FinnhubListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class FinnhubListFragment : BaseDataBindingFragment<FragmentFinnhubListBinding>(R.layout.fragment_finnhub_list) {

    private val finnhubListViewModel: FinnhubListViewModel by viewModels()
    private lateinit var finnhubListAdapter : FinnhubListAdapter

    override fun setDataBoundViewModels(binding: FragmentFinnhubListBinding) {
        binding.finnhubListViewModel = finnhubListViewModel
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
                finnhubListViewModel.navigationInstructions.collectLatest {
                    findNavController().executeNavInstructions(it)
                }
            }
        }
    }

    private fun setupRecycler() {
        finnhubListAdapter = FinnhubListAdapter(viewLifecycleOwner)
        finnhubListAdapter.addItemClickListener(finnhubListViewModel)

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

        val refreshStateAdapter = LoadStateAdapter(finnhubListAdapter)
        val withLoadStateFooter = finnhubListAdapter.withLoadStateFooter(LoadStateAdapter(finnhubListAdapter))
        withLoadStateFooter.addAdapter(0, refreshStateAdapter)
        finnhubListAdapter.addLoadStateListener {
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
            finnhubListAdapter.refresh()
        }

        binding.mainListRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainListRecycler.adapter = withLoadStateFooter
        finnhubListViewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            finnhubListAdapter.setData(pagingData)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuRefresh -> {
                finnhubListAdapter.refresh()
            }
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