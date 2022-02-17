package ru.nikitaartamonov.rickandmorty.domain.entities

data class EntityPage<T>(
    val info: PageInfo,
    val results: List<T>
)
