package ru.nikitaartamonov.rickandmorty.ui.pages.locations

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nikitaartamonov.rickandmorty.R
import ru.nikitaartamonov.rickandmorty.databinding.FragmentLocationsBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationsFilterState

class LocationsFragment : Fragment(R.layout.fragment_locations) {

    private val binding by viewBinding(FragmentLocationsBinding::bind)
    private val viewModel: LocationsContract.ViewModel by viewModels<LocationsViewModel>()

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
        binding.locationsShadowFrameLayout.setOnClickListener { showOrHideFilterMenu() }
        initFilterListener()
        binding.locationsRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            binding.locationsRefreshLayout.isRefreshing = false
        }
    }

    private fun initFilterListener() {
        binding.locationsFilters.locationsTypeFilterEditText.addTextChangedListener {
            viewModel.onFiltersStateChange(
                LocationsFilterState.Companion.FilterType.TYPE,
                binding.locationsFilters.locationsTypeFilterEditText.text.toString()
            )
        }
        binding.locationsFilters.locationsDimensionFilterEditText.addTextChangedListener {
            viewModel.onFiltersStateChange(
                LocationsFilterState.Companion.FilterType.DIMENSION,
                binding.locationsFilters.locationsDimensionFilterEditText.text.toString()
            )
        }
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.renderLocationsListLiveData.observe(viewLifecycleOwner) { adapter.updateList(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.emptyResponseLiveData.observe(viewLifecycleOwner) { setEmptyResponseMode(it) }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { openLocationDetails(it) }
        }
    }

    private fun openLocationDetails(id: Int) {
        //todo
    }

    private fun setEmptyResponseMode(isVisible: Boolean) {
        binding.locationsEmptyResponseTextView.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.locationsRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.locationsProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.locationsRecyclerView.adapter = adapter
        binding.locationsRecyclerView.addOnScrollListener(object :
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
        binding.locationsRetryButton.setOnClickListener { viewModel.onRetryButtonPressed() }
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
        val shadowIsVisible = binding.locationsShadowFrameLayout.isVisible
        binding.locationsShadowFrameLayout.visibility =
            if (shadowIsVisible) View.GONE else View.VISIBLE
        val filtersAreVisible = binding.locationsFilters.locationsFiltersCardView.isVisible
        binding.locationsFilters.locationsFiltersCardView.visibility =
            if (filtersAreVisible) View.GONE else View.VISIBLE
        hideKeyBoard()
    }

    private fun hideKeyBoard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}