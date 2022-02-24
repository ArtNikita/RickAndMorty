package ru.nikitaartamonov.rickandmorty.ui.pages.character_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.nikitaartamonov.rickandmorty.App
import ru.nikitaartamonov.rickandmorty.R
import ru.nikitaartamonov.rickandmorty.data.Constants
import ru.nikitaartamonov.rickandmorty.domain.Event
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener
import ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view.EpisodesAdapter

class CharactersDetailsViewModel(application: Application) : AndroidViewModel(application),
    CharactersDetailsContract.ViewModel {

    private var characterEntityIsLoaded = false
    private lateinit var characterEntity: CharacterEntity
    private var episodesFullListDownloaded = false
    private var episodesList: List<EpisodeEntity> = emptyList()
        private set(value) {
            field = value
            renderEpisodesListLiveData.postValue(
                EntityPage(PageInfo(-1, -1, "-1", null), value)
            )
        }

    private val listener = object : OnItemClickListener {
        override fun <T : IdentifiedEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }

    override val adapter: EpisodesAdapter = EpisodesAdapter(listener)
    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdentifiedEntity>> = MutableLiveData()
    override val openLocationDetailsLiveData: LiveData<Event<LocationEntity>> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderCharacterEntityLiveData: LiveData<CharacterEntity> = MutableLiveData()
    override val renderEpisodesListLiveData: LiveData<EntityPage<EpisodeEntity>> = MutableLiveData()

    private var errorMode = false
        set(value) {
            field = value
            setErrorModeLiveData.postValue(value)
        }

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(value)
        }

    override fun onViewCreated(id: Int) {
        loadData(id)
    }

    override fun onRetryButtonPressed(id: Int) {
        loadData(id)
    }

    override fun onOriginLinkPressed() {
        if (characterEntity.origin.name != getApplication<App>().getString(R.string.unknown)) {
            val event = Event(
                LocationEntity(
                    characterEntity.origin.url.split(Constants.LOCATION_URL_START)[1].toInt(),
                    characterEntity.origin.name,
                    "", "", emptyList(), ""
                )
            )
            openLocationDetailsLiveData.postValue(event)
        }
    }

    override fun onLocationLinkPressed() {
        if (characterEntity.location.name != getApplication<App>().getString(R.string.unknown)) {
            val event = Event(
                LocationEntity(
                    characterEntity.location.url.split(Constants.LOCATION_URL_START)[1].toInt(),
                    characterEntity.location.name,
                    "", "", emptyList(), ""
                )
            )
            openLocationDetailsLiveData.postValue(event)
        }
    }

    private fun loadData(id: Int) {
        if (!characterEntityIsLoaded) {
            loadCharacterEntity(id)
        } else if (!episodesFullListDownloaded) {
            loadEpisodes()
        }
    }

    private fun loadEpisodes() {
        isLoading = true
        compositeDisposable.add(
            getApplication<App>().appComponent.getNetworkApi()
                .getEpisodesByIds(generateEpisodesIdsString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        episodesList = it
                        episodesFullListDownloaded = true
                        errorMode = false
                        isLoading = false
                    },
                    onError = {
                        loadEpisodesFromRepo()
                    }
                )
        )
    }

    private fun loadEpisodesFromRepo() {
        compositeDisposable.add(
            getApplication<App>().appComponent.getEpisodesRepo()
                .getByIds(generateEpisodesIdsList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        episodesList = it
                        val loadedListSize = it.size
                        if (loadedListSize == characterEntity.episode.size) {
                            episodesFullListDownloaded = true
                            errorMode = false
                        } else {
                            errorMode = true
                        }
                        isLoading = false
                    },
                    onError = {
                        errorMode = true
                        isLoading = false
                    }
                )
        )
    }

    private fun loadCharacterEntity(id: Int) {
        isLoading = true
        compositeDisposable.add(
            getApplication<App>().appComponent.getNetworkApi()
                .getCharacterById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        characterEntity = it
                        characterEntityIsLoaded = true
                        loadData(id)
                        errorMode = false
                        renderCharacterEntityLiveData.postValue(it)
                        isLoading = false
                    },
                    onError = { loadCharacterFromRepo(id) }
                )
        )
    }

    private fun loadCharacterFromRepo(id: Int) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getCharactersRepo()
                .getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        characterEntity = it
                        characterEntityIsLoaded = true
                        loadData(id)
                        errorMode = false
                        renderCharacterEntityLiveData.postValue(it)
                        isLoading = false
                    },
                    onError = {
                        errorMode = true
                        isLoading = false
                    }
                )
        )
    }

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun generateEpisodesIdsString(): String {
        val episodesListObservable = Observable.fromIterable(characterEntity.episode)
        val episodesIds = StringBuilder()
        episodesListObservable
            .map {
                it.split(Constants.EPISODE_URL_START)[1]
            }
            .subscribe {
                episodesIds.append(it).append(",")
            }
        return episodesIds.toString()
    }

    private fun generateEpisodesIdsList(): List<Int> {
        val episodesListObservable = Observable.fromIterable(characterEntity.episode)
        var episodesIds = ArrayList<Int>()
        episodesListObservable
            .map {
                it.split(Constants.EPISODE_URL_START)[1].toInt()
            }
            .toList()
            .subscribeBy(
                onSuccess = {
                    episodesIds = it as ArrayList<Int>
                }
            )
        return episodesIds
    }
}

private fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData<T>).postValue(value)
}