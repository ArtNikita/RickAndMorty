package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

interface CharactersContract {

    interface ViewModel {

        fun onViewCreated()

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val renderCharactersListLiveData: LiveData<List<CharacterEntity>>
    }
}