package ru.nikitaartamonov.rickandmorty.impl

import io.reactivex.rxjava3.core.Flowable
import ru.nikitaartamonov.rickandmorty.data.room.CharacterDao
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.repos.CharactersRepo

class CharactersRepoRoom(private val characterDao: CharacterDao) : CharactersRepo {

    override fun add(character: CharacterEntity) {
        characterDao.add(character)
    }

    override fun addAll(characters: List<CharacterEntity>) {
        characterDao.addAll(characters)
    }

    override fun getAll(): Flowable<List<CharacterEntity>> = characterDao.getAll()
}