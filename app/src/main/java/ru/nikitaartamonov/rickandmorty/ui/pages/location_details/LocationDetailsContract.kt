package ru.nikitaartamonov.rickandmorty.ui.pages.location_details

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.Event
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity
import ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view.CharactersAdapter

interface LocationDetailsContract {

    interface ViewModel {

        fun onViewCreated(id: Int)
        fun onRetryButtonPressed(id: Int)

        val adapter: CharactersAdapter

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val renderLocationEntityLiveData: LiveData<LocationEntity>
        val renderCharactersListLiveData: LiveData<EntityPage<CharacterEntity>>
        val openEntityDetailsLiveData: LiveData<Event<IdentifiedEntity>>
    }
}