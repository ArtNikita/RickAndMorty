package ru.nikitaartamonov.rickandmorty.ui.pages.locations.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.databinding.ItemLocationBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity

class LocationsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    ItemLocationBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ).root
) {

    private val binding = ItemLocationBinding.bind(itemView)

    fun bind(locationEntity: LocationEntity) {
        binding.itemLocationNameTextView.text = locationEntity.name
        binding.itemLocationTypeTextView.text = locationEntity.type
        binding.itemLocationDimensionTextView.text = locationEntity.dimension
    }
}