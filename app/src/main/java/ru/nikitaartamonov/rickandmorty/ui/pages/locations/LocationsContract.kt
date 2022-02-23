package ru.nikitaartamonov.rickandmorty.ui.pages.locations

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationsFilterState
import ru.nikitaartamonov.rickandmorty.domain.view_model.GenericContract
import ru.nikitaartamonov.rickandmorty.ui.pages.locations.recycler_view.LocationsAdapter

class LocationsContract {

    interface ViewModel : GenericContract.ViewModel {

        fun onFiltersStateChange(
            filterType: LocationsFilterState.Companion.FilterType,
            filterName: String
        )

        val renderLocationsListLiveData: LiveData<EntityPage<LocationEntity>>
        val adapter: LocationsAdapter
    }
}