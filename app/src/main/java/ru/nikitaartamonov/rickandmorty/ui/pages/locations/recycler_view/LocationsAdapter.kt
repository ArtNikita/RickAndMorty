package ru.nikitaartamonov.rickandmorty.ui.pages.locations.recycler_view

import android.view.ViewGroup
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.GenericAdapter
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener

class LocationsAdapter(listener: OnItemClickListener) :
    GenericAdapter<LocationEntity, LocationsViewHolder>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}