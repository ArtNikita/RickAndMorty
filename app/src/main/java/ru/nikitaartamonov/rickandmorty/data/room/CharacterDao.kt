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

    @Query("SELECT * FROM characterentity WHERE name LIKE '%' || :name || '%' AND species LIKE '%' || :species || '%' AND status LIKE '%' || :status || '%' AND gender LIKE '%' || :gender || '%' LIMIT :limit OFFSET :offset")
    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        species: String,
        status: String,
        gender: String
    ): Single<List<CharacterEntity>>
}