package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharactersFilterState
import ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view.CharactersAdapter

interface CharactersContract {

    interface ViewModel {

        fun onRecyclerViewScrolledDown()
        fun onRetryButtonPressed()
        fun onQueryTextChange(text: String)
        fun onFiltersStateChange(
            filterType: CharactersFilterState.Companion.FilterType,
            filterName: String?
        )

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val renderCharactersListLiveData: LiveData<EntityPage<CharacterEntity>>

        val adapter: CharactersAdapter
    }
}