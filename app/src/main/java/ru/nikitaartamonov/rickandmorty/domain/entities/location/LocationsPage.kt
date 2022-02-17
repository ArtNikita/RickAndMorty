package ru.nikitaartamonov.rickandmorty.domain.entities.location

import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo

data class LocationsPage(
    val info: PageInfo,
    val results: List<LocationEntity>
)