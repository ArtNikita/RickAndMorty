package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nikitaartamonov.rickandmorty.R
import ru.nikitaartamonov.rickandmorty.databinding.FragmentCharactersBinding

class CharactersFragment : Fragment(R.layout.fragment_characters) {

    private val binding by viewBinding(FragmentCharactersBinding::bind)
    private val viewModel: CharactersContract.ViewModel by viewModels<CharactersViewModel>()

    private val adapter by lazy { viewModel.adapter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.renderCharactersListLiveData.observe(viewLifecycleOwner) { adapter.updateList(it) }
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
}