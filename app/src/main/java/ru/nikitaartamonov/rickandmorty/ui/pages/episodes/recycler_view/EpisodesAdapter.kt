package ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view

import android.view.ViewGroup
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.GenericAdapter

class EpisodesAdapter : GenericAdapter<EpisodeEntity, EpisodesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}