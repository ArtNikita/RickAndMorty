package ru.nikitaartamonov.rickandmorty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = IGNORE)
    fun add(character: CharacterEntity): Single<Long>

    @Insert(onConflict = IGNORE)
    fun addAll(characters: List<CharacterEntity>): Completable

    @Query("SELECT * FROM characterentity LIMIT :limit OFFSET :offset")
    fun getAll(limit: Int, offset: Int): Single<List<CharacterEntity>>
}