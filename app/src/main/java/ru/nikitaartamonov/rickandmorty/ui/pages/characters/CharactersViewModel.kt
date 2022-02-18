package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CharactersViewModel(application: Application) : AndroidViewModel(application),
    CharactersContract.ViewModel {

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(field)
        }

    override fun onViewIsCreated() {
        //todo load and render
    }

    override fun onCleared() {
        super.onCleared()
        //todo dispose composite disposable
    }
}

private fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData<T>).postValue(value)
}