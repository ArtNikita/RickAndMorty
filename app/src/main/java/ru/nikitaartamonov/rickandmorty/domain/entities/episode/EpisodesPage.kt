package ru.nikitaartamonov.rickandmorty.domain.entities.episode

import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo

data class EpisodesPage(
    val info: PageInfo,
    val results: List<EpisodeEntity>
)