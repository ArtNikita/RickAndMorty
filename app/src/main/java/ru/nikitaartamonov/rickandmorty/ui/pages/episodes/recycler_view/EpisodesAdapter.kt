package ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view

import android.view.ViewGroup
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.GenericAdapter
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener

class EpisodesAdapter(listener: OnItemClickListener) :
    GenericAdapter<EpisodeEntity, EpisodesViewHolder>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}