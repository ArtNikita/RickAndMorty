package ru.nikitaartamonov.rickandmorty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = IGNORE)
    fun add(character: CharacterEntity)

    @Insert(onConflict = IGNORE)
    fun addAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characterentity")
    fun getAll(): Flowable<List<CharacterEntity>>
}