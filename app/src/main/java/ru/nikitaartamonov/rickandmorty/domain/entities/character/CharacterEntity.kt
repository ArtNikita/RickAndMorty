package ru.nikitaartamonov.rickandmorty.domain.entities.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity

@Entity
data class CharacterEntity(
    @PrimaryKey override val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded(prefix = "origin") val origin: CharacterOrigin,
    @Embedded(prefix = "location") val location: CharacterLocation,
    val image: String,
    val episode: List<String>,
    val url: String
) : IdentifiedEntity