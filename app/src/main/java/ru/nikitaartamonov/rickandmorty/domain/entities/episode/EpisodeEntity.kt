package ru.nikitaartamonov.rickandmorty.domain.entities.episode

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity

@Entity
data class EpisodeEntity(
    @PrimaryKey override val id: Int,
    override val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String
) : IdentifiedEntity