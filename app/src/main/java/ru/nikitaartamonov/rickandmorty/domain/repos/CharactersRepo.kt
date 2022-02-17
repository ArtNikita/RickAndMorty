package ru.nikitaartamonov.rickandmorty.domain.repos

import io.reactivex.rxjava3.core.Flowable
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

interface CharactersRepo {

    fun add(character: CharacterEntity)

    fun addAll(characters: List<CharacterEntity>)

    fun getAll(): Flowable<List<CharacterEntity>>
}