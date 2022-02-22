package ru.nikitaartamonov.rickandmorty.domain.repos

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity

interface LocationsRepo {

    fun add(location: LocationEntity): Single<Long>

    fun addAll(locations: List<LocationEntity>): Completable

    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        type: String,
        dimension: String,
    ): Single<List<LocationEntity>>
}