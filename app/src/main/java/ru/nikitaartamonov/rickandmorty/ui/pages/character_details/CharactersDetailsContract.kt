package ru.nikitaartamonov.rickandmorty.ui.pages.character_details

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.Event
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity
import ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view.EpisodesAdapter

interface CharactersDetailsContract {

    interface ViewModel {

        fun onViewCreated(id: Int)
        fun onRetryButtonPressed(id: Int)
        fun onOriginLinkPressed()
        fun onLocationLinkPressed()

        val adapter: EpisodesAdapter

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val renderCharacterEntityLiveData: LiveData<CharacterEntity>
        val renderEpisodesListLiveData: LiveData<EntityPage<EpisodeEntity>>
        val openEntityDetailsLiveData: LiveData<Event<IdentifiedEntity>>
        val openLocationDetailsLiveData: LiveData<Event<LocationEntity>>
    }
}