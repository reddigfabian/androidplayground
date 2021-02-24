package com.fabian.androidplayground.ui.main.list.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.LoremPicsumRepository
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.recyclerview.LoadStateAdapter
import com.fabian.androidplayground.common.utils.UIUtils
import com.fabian.androidplayground.databinding.FragmentListBinding
import com.fabian.androidplayground.ui.main.list.MainListAdapter
import com.fabian.androidplayground.ui.main.list.viewmodels.ListViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "ListFragment"

@FlowPreview
@ExperimentalCoroutinesApi
class ListFragment : BaseDataBindingFragment<FragmentListBinding>(R.layout.fragment_list) {

    private val listViewModel : ListViewModel by activityViewModels { ListViewModel.Factory(LoremPicsumRepository) }

    override fun setDataBoundViewModels(binding: FragmentListBinding) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.listViewModel = listViewModel
        viewLifecycleOwner.lifecycle.addObserver(listViewModel)
    }

    private var job : Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mainListAdapter = MainListAdapter()
        listViewModel.setItemClickFlow(mainListAdapter.itemClickFlow)

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
//            if (listViewModel.swipeRefreshing.value != true) {
//                refreshStateAdapter.loadState = refresh
//            } else {
//                if (refresh is LoadState.NotLoading) {
//                    listViewModel.swipeRefreshing.value = false
//                }
//            }
            Log.d(TAG, "onViewCreated: $it")

            when (refresh) {
                is LoadState.NotLoading -> {
                    binding.loadingAnimationView.visibility = View.GONE
                }
                is LoadState.Loading -> {
                    binding.loadingAnimationView.visibility = View.VISIBLE
                }
                is LoadState.Error -> {}
            }
        }
        val withLoadStateFooter = mainListAdapter.withLoadStateFooter(LoadStateAdapter(mainListAdapter))
        withLoadStateFooter.addAdapter(0, refreshStateAdapter)

        binding.mainListRecycler.layoutManager = StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL)
        binding.mainListRecycler.adapter = mainListAdapter
        job?.cancel()
        job = lifecycleScope.async {
            listViewModel.images.collectLatest {
                listViewModel.swipeRefreshing.postValue(false)
                mainListAdapter.submitData(it)
            }
        }
    }
}