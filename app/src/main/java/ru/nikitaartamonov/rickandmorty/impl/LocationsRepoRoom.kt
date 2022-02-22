package ru.nikitaartamonov.rickandmorty.impl

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitaartamonov.rickandmorty.data.room.LocationDao
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.repos.LocationsRepo

class LocationsRepoRoom(private val locationDao: LocationDao) : LocationsRepo {

    override fun add(location: LocationEntity): Single<Long> =
        locationDao.add(location).subscribeOn(Schedulers.io())


    override fun addAll(locations: List<LocationEntity>): Completable =
        locationDao.addAll(locations).subscribeOn(Schedulers.io())


    override fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        type: String,
        dimension: String
    ): Single<List<LocationEntity>> =
        locationDao.getAll(limit, offset, name, type, dimension).subscribeOn(Schedulers.io())
}