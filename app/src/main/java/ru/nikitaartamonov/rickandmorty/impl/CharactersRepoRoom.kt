package ru.nikitaartamonov.rickandmorty.impl

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.nikitaartamonov.rickandmorty.data.room.CharacterDao
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.repos.CharactersRepo

class CharactersRepoRoom(private val characterDao: CharacterDao) : CharactersRepo {

    override fun add(character: CharacterEntity): Single<Long> =
        characterDao.add(character).subscribeOn(Schedulers.io())


    override fun addAll(characters: List<CharacterEntity>): Completable =
        characterDao.addAll(characters).subscribeOn(Schedulers.io())


    override fun getAll(limit: Int, offset: Int): Single<List<CharacterEntity>> =
        characterDao.getAll(limit, offset).subscribeOn(Schedulers.io())
}