package ru.nikitaartamonov.rickandmorty.domain.repos

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

interface CharactersRepo {

    fun add(character: CharacterEntity): Single<Long>

    fun addAll(characters: List<CharacterEntity>): Completable

    fun getAll(limit: Int, offset: Int): Single<List<CharacterEntity>>
}