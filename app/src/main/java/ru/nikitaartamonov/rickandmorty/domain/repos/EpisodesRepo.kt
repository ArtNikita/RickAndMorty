package ru.nikitaartamonov.rickandmorty.domain.repos

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity

interface EpisodesRepo {

    fun add(episode: EpisodeEntity): Single<Long>

    fun addAll(episodes: List<EpisodeEntity>): Completable

    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        episode: String,
    ): Single<List<EpisodeEntity>>
}