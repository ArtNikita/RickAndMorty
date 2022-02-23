package ru.nikitaartamonov.rickandmorty.domain.view_model

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.Event

interface GenericContract {

    interface ViewModel {

        fun onRecyclerViewScrolledDown()
        fun onRetryButtonPressed()
        fun onQueryTextChange(text: String)
        fun onRefresh()

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val emptyResponseLiveData: LiveData<Boolean>
        val openEntityDetailsLiveData: LiveData<Event<Int>>
    }
}