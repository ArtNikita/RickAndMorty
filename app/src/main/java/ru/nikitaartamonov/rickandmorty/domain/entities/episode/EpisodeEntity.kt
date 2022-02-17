package ru.nikitaartamonov.rickandmorty.domain.entities.episode

data class EpisodeEntity(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String
)