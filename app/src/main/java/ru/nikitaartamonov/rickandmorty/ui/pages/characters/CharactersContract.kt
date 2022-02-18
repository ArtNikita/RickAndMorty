package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import androidx.lifecycle.LiveData

interface CharactersContract {

    interface ViewModel {

        fun onViewIsCreated()

        val showLoadingIndicatorLiveData: LiveData<Boolean>
    }
}