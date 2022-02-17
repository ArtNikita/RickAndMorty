package ru.nikitaartamonov.rickandmorty.domain.entities.character

import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo

data class CharactersPage(
    val info: PageInfo,
    val results: List<CharacterEntity>
)