package ru.nikitaartamonov.rickandmorty.domain.entities.location

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity

@Entity
data class LocationEntity(
    @PrimaryKey override val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String
) : IdentifiedEntity