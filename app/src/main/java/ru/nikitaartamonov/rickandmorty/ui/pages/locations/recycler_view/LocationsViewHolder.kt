package ru.nikitaartamonov.rickandmorty.ui.pages.locations.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.databinding.ItemLocationBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener

class LocationsViewHolder(parent: ViewGroup, listener: OnItemClickListener) :
    RecyclerView.ViewHolder(
        ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {

    init {
        itemView.setOnClickListener { listener.onClick(currentLocation) }
    }

    private val binding = ItemLocationBinding.bind(itemView)
    private lateinit var currentLocation: LocationEntity

    fun bind(locationEntity: LocationEntity) {
        currentLocation = locationEntity
        binding.itemLocationNameTextView.text = locationEntity.name
        binding.itemLocationTypeTextView.text = locationEntity.type
        binding.itemLocationDimensionTextView.text = locationEntity.dimension
    }
}