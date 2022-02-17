package ru.nikitaartamonov.rickandmorty.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class DataBase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}