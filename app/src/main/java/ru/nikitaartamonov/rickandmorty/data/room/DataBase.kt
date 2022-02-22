package ru.nikitaartamonov.rickandmorty.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity

@Database(
    entities = [CharacterEntity::class, EpisodeEntity::class, LocationEntity::class],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class DataBase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun episodesDao(): EpisodeDao
    abstract fun locationsDao(): LocationDao
}