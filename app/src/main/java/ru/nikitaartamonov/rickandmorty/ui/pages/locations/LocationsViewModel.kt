package ru.nikitaartamonov.rickandmorty.ui.pages.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.nikitaartamonov.rickandmorty.App
import ru.nikitaartamonov.rickandmorty.data.Constants
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.PageInfo
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationsFilterState
import ru.nikitaartamonov.rickandmorty.ui.pages.locations.recycler_view.LocationsAdapter

class LocationsViewModel(application: Application) : AndroidViewModel(application),
    LocationsContract.ViewModel {

    override val adapter: LocationsAdapter = LocationsAdapter()

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val emptyResponseLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderLocationsListLiveData: LiveData<EntityPage<LocationEntity>> =
        MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private val locationsFilterState = LocationsFilterState()

    private var locationsPage: EntityPage<LocationEntity> =
        EntityPage(PageInfo(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderLocationsListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) {
            loadLocations(
                page = ++pageToLoadNumber,
                locationsFilterState
            )
        }
    }

    override fun onRetryButtonPressed() {
        loadLocations(page = pageToLoadNumber, locationsFilterState)
    }

    override fun onQueryTextChange(text: String) {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        locationsFilterState.name = text
        loadLocations(page = pageToLoadNumber, locationsFilterState)
    }

    override fun onFiltersStateChange(
        filterType: LocationsFilterState.Companion.FilterType,
        filterName: String
    ) {
        when (filterType) {
            LocationsFilterState.Companion.FilterType.TYPE -> {
                locationsFilterState.type = filterName
            }
            LocationsFilterState.Companion.FilterType.DIMENSION -> {
                locationsFilterState.dimension = filterName
            }
        }
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadLocations(page = pageToLoadNumber, locationsFilterState)
    }

    override fun onRefresh() {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadLocations(page = pageToLoadNumber, locationsFilterState)
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
        loadLocations(page = pageToLoadNumber, locationsFilterState)
    }

    private fun loadLocations(
        page: Int? = null,
        locationsFilterState: LocationsFilterState
    ) {
        isLoading = true
        isEmptyResponse = false
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getLocationsPage(
                page,
                locationsFilterState.name,
                locationsFilterState.type,
                locationsFilterState.dimension
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    locationsPage = it
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
                        locationsPage = EntityPage(PageInfo(-1, -1, null, null), emptyList())
                    } else {
                        errorMode = true
                        loadFromRoomRepo()
                    }
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getLocationRepo().getAll(
            Constants.ENTITY_PAGE_SIZE, Constants.ENTITY_PAGE_SIZE * lastLoadedPageNumber,
            locationsFilterState.name,
            locationsFilterState.type ?: "",
            locationsFilterState.dimension ?: ""
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    locationsPage = EntityPage(PageInfo(-1, -1, "-1", prevPage), it)
                    if (it.isEmpty() && lastLoadedPageNumber == 0) isEmptyResponse = true
                }
            )
    }

    private fun saveToLocalRepo(page: EntityPage<LocationEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getLocationRepo().addAll(page.results).subscribe()
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