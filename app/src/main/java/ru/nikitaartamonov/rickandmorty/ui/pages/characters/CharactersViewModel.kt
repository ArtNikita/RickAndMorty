package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.nikitaartamonov.rickandmorty.App
import ru.nikitaartamonov.rickandmorty.data.Constants
import ru.nikitaartamonov.rickandmorty.domain.Event
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharactersFilterState
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.IdentifiedEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener
import ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view.CharactersAdapter

class CharactersViewModel(application: Application) : AndroidViewModel(application),
    CharactersContract.ViewModel {

    private val listener = object : OnItemClickListener {
        override fun <T : IdentifiedEntity> onClick(entity: T) {
            openEntityDetailsLiveData.postValue(Event(entity))
        }
    }
    override val adapter: CharactersAdapter = CharactersAdapter(listener)

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val emptyResponseLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderCharactersListLiveData: LiveData<EntityPage<CharacterEntity>> =
        MutableLiveData()
    override val openEntityDetailsLiveData: LiveData<Event<IdentifiedEntity>> = MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private val charactersFilterState = CharactersFilterState()

    private var charactersPage: EntityPage<CharacterEntity> =
        EntityPage(PageInfo(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderCharactersListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) {
            loadCharacters(
                page = ++pageToLoadNumber,
                charactersFilterState
            )
        }
    }

    override fun onRetryButtonPressed() {
        loadCharacters(page = pageToLoadNumber, charactersFilterState)
    }

    override fun onQueryTextChange(text: String) {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        charactersFilterState.name = text
        loadCharacters(page = pageToLoadNumber, charactersFilterState)
    }

    override fun onFiltersStateChange(
        filterType: CharactersFilterState.Companion.FilterType,
        filterName: String?
    ) {
        when (filterType) {
            CharactersFilterState.Companion.FilterType.SPECIES -> {
                charactersFilterState.species = filterName
            }
            CharactersFilterState.Companion.FilterType.STATUS -> {
                charactersFilterState.status = filterName
            }
            CharactersFilterState.Companion.FilterType.GENDER -> {
                charactersFilterState.gender = filterName
            }
        }
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadCharacters(page = pageToLoadNumber, charactersFilterState)
    }

    override fun onRefresh() {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadCharacters(page = pageToLoadNumber, charactersFilterState)
    }

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(value)
        }

    private var errorMode = false
        set(value) {
            field = value
            setErrorModeLiveData.postValue(value)
        }

    private var isEmptyResponse = false
        set(value) {
            field = value
            emptyResponseLiveData.postValue(value)
        }

    private var compositeDisposable = CompositeDisposable()

    init {
        loadCharacters(page = pageToLoadNumber, charactersFilterState)
    }

    private fun loadCharacters(
        page: Int? = null,
        charactersFilterState: CharactersFilterState
    ) {
        isLoading = true
        isEmptyResponse = false
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getCharacterPage(
                page,
                charactersFilterState.name,
                charactersFilterState.status,
                charactersFilterState.species,
                charactersFilterState.gender
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    charactersPage = it
                    lastLoadedPageNumber = pageToLoadNumber
                    saveToLocalRepo(it)
                    isLoading = false
                    errorMode = false
                },
                onError = {
                    if (it.message?.contains(Constants.EMPTY_RESPONSE_MESSAGE) != false) {
                        isLoading = false
                        errorMode = false
                        if (lastLoadedPageNumber != 0) return@subscribeBy
                        isEmptyResponse = true
                        charactersPage = EntityPage(PageInfo(-1, -1, null, null), emptyList())
                    } else {
                        errorMode = true
                        loadFromRoomRepo()
                    }
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getCharactersRepo().getAll(
            Constants.ENTITY_PAGE_SIZE, Constants.ENTITY_PAGE_SIZE * lastLoadedPageNumber,
            charactersFilterState.name,
            charactersFilterState.species ?: "",
            charactersFilterState.status ?: "",
            charactersFilterState.gender ?: ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    charactersPage = EntityPage(PageInfo(-1, -1, "-1", prevPage), it)
                    if (it.isEmpty() && lastLoadedPageNumber == 0) isEmptyResponse = true
                }
            )
    }

    private fun saveToLocalRepo(page: EntityPage<CharacterEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getCharactersRepo().addAll(page.results).subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

private fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData<T>).postValue(value)
}