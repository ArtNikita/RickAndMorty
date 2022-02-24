package ru.nikitaartamonov.rickandmorty.ui.pages.episode_details

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.Event
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity
import ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view.CharactersAdapter

interface EpisodeDetailsContract {

    interface ViewModel {

        fun onViewCreated(id: Int)
        fun onRetryButtonPressed(id: Int)

        val adapter: CharactersAdapter

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val renderEpisodeEntityLiveData: LiveData<EpisodeEntity>
        val renderCharactersListLiveData: LiveData<EntityPage<CharacterEntity>>
        val openEntityDetailsLiveData: LiveData<Event<IdentifiedEntity>>
    }
}