package ru.nikitaartamonov.rickandmorty.domain.entities.location

data class LocationEntity(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String
)