package ru.nikitaartamonov.rickandmorty.domain.view_model

import androidx.lifecycle.LiveData

interface GenericContract {

    interface ViewModel {

        fun onRecyclerViewScrolledDown()
        fun onRetryButtonPressed()
        fun onQueryTextChange(text: String)
        fun onRefresh()

        val showLoadingIndicatorLiveData: LiveData<Boolean>
        val setErrorModeLiveData: LiveData<Boolean>
        val emptyResponseLiveData: LiveData<Boolean>
    }
}