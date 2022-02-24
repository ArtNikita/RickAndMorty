package ru.nikitaartamonov.rickandmorty.ui.pages.location_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.nikitaartamonov.rickandmorty.App
import ru.nikitaartamonov.rickandmorty.data.Constants
import ru.nikitaartamonov.rickandmorty.domain.Event
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener
import ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view.CharactersAdapter

class LocationDetailsViewModel(application: Application) : AndroidViewModel(application),
    LocationDetailsContract.ViewModel {

    private var locationEntityIsLoaded = false
    private lateinit var locationEntity: LocationEntity
    private var charactersFullListDownloaded = false
    private var charactersList: List<CharacterEntity> = emptyList()
        private set(value) {
            field = value
            renderCharactersListLiveData.postValue(
                EntityPage(PageInfo(-1, -1, "-1", null), value)
            )
        }

    private val listener = object : OnItemClickListener {
        override fun <T : IdentifiedEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }

    override val adapter: CharactersAdapter = CharactersAdapter(listener)
    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdentifiedEntity>> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderLocationEntityLiveData: LiveData<LocationEntity> = MutableLiveData()
    override val renderCharactersListLiveData: LiveData<EntityPage<CharacterEntity>> =
        MutableLiveData()

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

    private fun loadData(id: Int) {
        if (!locationEntityIsLoaded) {
            loadLocationEntity(id)
        } else if (!charactersFullListDownloaded) {
            loadCharacters()
        }
    }

    private fun loadCharacters() {
        isLoading = true
        compositeDisposable.add(
            getApplication<App>().appComponent.getNetworkApi()
                .getCharactersByIds(generateCharactersIdsString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        charactersList = it
                        charactersFullListDownloaded = true
                        errorMode = false
                        isLoading = false
                    },
                    onError = {
                        loadCharactersFromRepo()
                    }
                )
        )
    }

    private fun loadCharactersFromRepo() {
        compositeDisposable.add(
            getApplication<App>().appComponent.getCharactersRepo()
                .getByIds(generateCharactersIdsList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        charactersList = it
                        val loadedListSize = it.size
                        if (loadedListSize == locationEntity.residents.size) {
                            charactersFullListDownloaded = true
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

    private fun loadLocationEntity(id: Int) {
        isLoading = true
        compositeDisposable.add(
            getApplication<App>().appComponent.getNetworkApi()
                .getLocationById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        locationEntity = it
                        locationEntityIsLoaded = true
                        loadData(id)
                        errorMode = false
                        renderLocationEntityLiveData.postValue(it)
                        isLoading = false
                    },
                    onError = { loadLocationFromRepo(id) }
                )
        )
    }

    private fun loadLocationFromRepo(id: Int) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getLocationRepo()
                .getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        locationEntity = it
                        locationEntityIsLoaded = true
                        loadData(id)
                        errorMode = false
                        renderLocationEntityLiveData.postValue(it)
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

    private fun generateCharactersIdsString(): String {
        val charactersListObservable = Observable.fromIterable(locationEntity.residents)
        val charactersIds = StringBuilder()
        charactersListObservable
            .map {
                it.split(Constants.CHARACTER_URL_START)[1]
            }
            .subscribe {
                charactersIds.append(it).append(",")
            }
        return charactersIds.toString()
    }

    private fun generateCharactersIdsList(): List<Int> {
        val charactersListObservable = Observable.fromIterable(locationEntity.residents)
        var charactersIds = ArrayList<Int>()
        charactersListObservable
            .map {
                it.split(Constants.CHARACTER_URL_START)[1].toInt()
            }
            .toList()
            .subscribeBy(
                onSuccess = {
                    charactersIds = it as ArrayList<Int>
                }
            )
        return charactersIds
    }
}

private fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData<T>).postValue(value)
}