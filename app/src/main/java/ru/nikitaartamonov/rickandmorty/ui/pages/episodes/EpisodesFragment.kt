package ru.nikitaartamonov.rickandmorty.ui.pages.episodes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nikitaartamonov.rickandmorty.R
import ru.nikitaartamonov.rickandmorty.databinding.FragmentEpisodesBinding

class EpisodesFragment : Fragment(R.layout.fragment_episodes) {

    private val binding by viewBinding(FragmentEpisodesBinding::bind)
    private val viewModel: EpisodesContract.ViewModel by viewModels<EpisodesViewModel>()

    private val adapter by lazy { viewModel.adapter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        initRecyclerView()
        initRetryButton()
        binding.episodesShadowFrameLayout.setOnClickListener { showOrHideFilterMenu() }
        initFilterListener()
        binding.episodesRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            binding.episodesRefreshLayout.isRefreshing = false
        }
    }

    private fun initFilterListener() {
        //todo
        //viewModel.onFilterStateChange("")
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.renderEpisodesListLiveData.observe(viewLifecycleOwner) { adapter.updateList(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.emptyResponseLiveData.observe(viewLifecycleOwner) { setEmptyResponseMode(it) }
    }

    private fun setEmptyResponseMode(isVisible: Boolean) {
        binding.episodesEmptyResponseTextView.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.episodesRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.episodesProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.episodesRecyclerView.adapter = adapter
        binding.episodesRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.onRecyclerViewScrolledDown()
                }
            }
        })
    }

    private fun initRetryButton() {
        binding.episodesRetryButton.setOnClickListener { viewModel.onRetryButtonPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tool_bar_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //do nothing
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onQueryTextChange(newText)
                return true
            }

        })
        menu.findItem(R.id.filters_menu).setOnMenuItemClickListener {
            showOrHideFilterMenu()
            true
        }
    }

    private fun showOrHideFilterMenu() {
        val shadowIsVisible = binding.episodesShadowFrameLayout.isVisible
        binding.episodesShadowFrameLayout.visibility =
            if (shadowIsVisible) View.GONE else View.VISIBLE
        //todo val filtersAreVisible = binding.episodesFilters.filtersCardView.isVisible
        //binding.charactersFilters.filtersCardView.visibility =
        //    if (filtersAreVisible) View.GONE else View.VISIBLE
    }
}