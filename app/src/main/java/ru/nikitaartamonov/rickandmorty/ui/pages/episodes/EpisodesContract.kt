package ru.nikitaartamonov.rickandmorty.ui.pages.episodes

import androidx.lifecycle.LiveData
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.view_model.GenericContract
import ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view.EpisodesAdapter

interface EpisodesContract {

    interface ViewModel : GenericContract.ViewModel {

        fun onFilterStateChange(episodeName: String)

        val renderEpisodesListLiveData: LiveData<EntityPage<EpisodeEntity>>
        val adapter: EpisodesAdapter
    }
}