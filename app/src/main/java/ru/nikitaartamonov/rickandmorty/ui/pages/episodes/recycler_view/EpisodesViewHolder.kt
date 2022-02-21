package ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.databinding.ItemEpisodeBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity

class EpisodesViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    ItemEpisodeBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ).root
) {

    private val binding = ItemEpisodeBinding.bind(itemView)

    fun bind(episodeEntity: EpisodeEntity) {
        binding.itemEpisodeNameTextView.text = episodeEntity.name
        binding.itemEpisodeNumberTextView.text = episodeEntity.episode
        binding.itemEpisodeAirDateTextView.text = episodeEntity.air_date
    }
}