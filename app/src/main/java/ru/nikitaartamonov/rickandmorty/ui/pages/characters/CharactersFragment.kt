package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import ru.nikitaartamonov.rickandmorty.R
import ru.nikitaartamonov.rickandmorty.databinding.FragmentCharactersBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharactersFilterState
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity


class CharactersFragment : Fragment(R.layout.fragment_characters) {

    private val binding by viewBinding(FragmentCharactersBinding::bind)
    private val viewModel: CharactersContract.ViewModel by viewModels<CharactersViewModel>()

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
        binding.charactersShadowFrameLayout.setOnClickListener { showOrHideFilterMenu() }
        initFilterListener()
        binding.charactersRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
            binding.charactersRefreshLayout.isRefreshing = false
        }
    }

    private fun initFilterListener() {
        binding.charactersFilters.speciesFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val checkedChip =
                binding.charactersFilters.speciesFilterChipGroup.findViewById<Chip>(checkedId)
            viewModel.onFiltersStateChange(
                CharactersFilterState.Companion.FilterType.SPECIES, checkedChip?.text?.toString()
            )
        }
        binding.charactersFilters.statusFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val checkedChip =
                binding.charactersFilters.statusFilterChipGroup.findViewById<Chip>(checkedId)
            viewModel.onFiltersStateChange(
                CharactersFilterState.Companion.FilterType.STATUS, checkedChip?.text?.toString()
            )
        }
        binding.charactersFilters.genderFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val checkedChip =
                binding.charactersFilters.genderFilterChipGroup.findViewById<Chip>(checkedId)
            viewModel.onFiltersStateChange(
                CharactersFilterState.Companion.FilterType.GENDER, checkedChip?.text?.toString()
            )
        }
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.renderCharactersListLiveData.observe(viewLifecycleOwner) { adapter.updateList(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.emptyResponseLiveData.observe(viewLifecycleOwner) { setEmptyResponseMode(it) }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { openCharacterDetails(it) }
        }
    }

    private fun openCharacterDetails(entity: IdentifiedEntity) {
        val direction =
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(
                entity.id,
                entity.name
            )
        findNavController().navigate(direction)
    }

    private fun setEmptyResponseMode(isVisible: Boolean) {
        binding.charactersEmptyResponseTextView.visibility =
            if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.charactersRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.charactersProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.charactersRecyclerView.adapter = adapter
        binding.charactersRecyclerView.addOnScrollListener(object :
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
        binding.charactersRetryButton.setOnClickListener { viewModel.onRetryButtonPressed() }
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
        val shadowIsVisible = binding.charactersShadowFrameLayout.isVisible
        binding.charactersShadowFrameLayout.visibility =
            if (shadowIsVisible) View.GONE else View.VISIBLE
        val filtersAreVisible = binding.charactersFilters.charactersFiltersCardView.isVisible
        binding.charactersFilters.charactersFiltersCardView.visibility =
            if (filtersAreVisible) View.GONE else View.VISIBLE
    }
}