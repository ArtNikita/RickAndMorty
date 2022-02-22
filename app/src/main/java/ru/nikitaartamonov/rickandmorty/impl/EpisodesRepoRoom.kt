package ru.nikitaartamonov.rickandmorty.impl

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitaartamonov.rickandmorty.data.room.EpisodeDao
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.repos.EpisodesRepo

class EpisodesRepoRoom(private val episodeDao: EpisodeDao) : EpisodesRepo {

    override fun add(episode: EpisodeEntity): Single<Long> =
        episodeDao.add(episode).subscribeOn(Schedulers.io())


    override fun addAll(episodes: List<EpisodeEntity>): Completable =
        episodeDao.addAll(episodes).subscribeOn(Schedulers.io())


    override fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        episode: String,
    ): Single<List<EpisodeEntity>> =
        episodeDao.getAll(limit, offset, name, episode).subscribeOn(Schedulers.io())
}