package ru.nikitaartamonov.rickandmorty.ui.pages.episode_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.nikitaartamonov.rickandmorty.R
import ru.nikitaartamonov.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity

class EpisodeDetailsFragment : Fragment(R.layout.fragment_episode_details) {

    private val args by navArgs<EpisodeDetailsFragmentArgs>()

    private val binding by viewBinding(FragmentEpisodeDetailsBinding::bind)
    private val viewModel: EpisodeDetailsContract.ViewModel by viewModels<EpisodeDetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        viewModel.onViewCreated(args.id)
    }

    private fun initViews() {
        binding.episodeDetailsRetryButton.setOnClickListener { viewModel.onRetryButtonPressed(args.id) }
        binding.episodeDetailsCharactersRecyclerView.adapter = viewModel.adapter
    }

    private fun initViewModel() {
        viewModel.showLoadingIndicatorLiveData.observe(viewLifecycleOwner) { showLoadingIndicator(it) }
        viewModel.setErrorModeLiveData.observe(viewLifecycleOwner) { setErrorMode(it) }
        viewModel.renderEpisodeEntityLiveData.observe(viewLifecycleOwner) {
            renderEpisodeEntity(it)
        }
        viewModel.renderCharactersListLiveData.observe(viewLifecycleOwner) {
            viewModel.adapter.updateList(it)
        }
        viewModel.openEntityDetailsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                val direction =
                    EpisodeDetailsFragmentDirections.actionEpisodeDetailsFragmentToCharacterDetailsFragment(
                        it.id, it.name
                    )
                findNavController().navigate(direction)
            }
        }
    }

    private fun renderEpisodeEntity(episodeEntity: EpisodeEntity) {
        val episodeNumberText = "${getString(R.string.episode)}: ${episodeEntity.episode}"
        val airDateText = "${getString(R.string.air_date)}: ${episodeEntity.air_date}"
        binding.episodeDetailsNumberTextView.text = episodeNumberText
        binding.episodeDetailsAirDateTextView.text = airDateText
    }

    private fun showLoadingIndicator(isVisible: Boolean) {
        binding.episodeDetailsProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setErrorMode(isVisible: Boolean) {
        binding.episodeDetailsRetryButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}