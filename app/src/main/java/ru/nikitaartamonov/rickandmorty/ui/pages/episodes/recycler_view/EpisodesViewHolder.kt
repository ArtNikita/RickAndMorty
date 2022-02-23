package ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.databinding.ItemEpisodeBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener

class EpisodesViewHolder(parent: ViewGroup, listener: OnItemClickListener) :
    RecyclerView.ViewHolder(
        ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {

    init {
        itemView.setOnClickListener { listener.onClick(currentEpisode) }
    }

    private val binding = ItemEpisodeBinding.bind(itemView)
    private lateinit var currentEpisode: EpisodeEntity

    fun bind(episodeEntity: EpisodeEntity) {
        currentEpisode = episodeEntity
        binding.itemEpisodeNameTextView.text = episodeEntity.name
        binding.itemEpisodeNumberTextView.text = episodeEntity.episode
        binding.itemEpisodeAirDateTextView.text = episodeEntity.air_date
    }
}