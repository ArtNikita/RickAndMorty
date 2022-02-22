package ru.nikitaartamonov.rickandmorty.ui.pages.episodes

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
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodesFilterState
import ru.nikitaartamonov.rickandmorty.ui.pages.episodes.recycler_view.EpisodesAdapter

class EpisodesViewModel(application: Application) : AndroidViewModel(application),
    EpisodesContract.ViewModel {

    override val adapter: EpisodesAdapter = EpisodesAdapter()

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val setErrorModeLiveData: LiveData<Boolean> = MutableLiveData()
    override val emptyResponseLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderEpisodesListLiveData: LiveData<EntityPage<EpisodeEntity>> =
        MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private val episodesFilterState = EpisodesFilterState()

    private var episodesPage: EntityPage<EpisodeEntity> =
        EntityPage(PageInfo(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderEpisodesListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) {
            loadEpisodes(
                page = ++pageToLoadNumber,
                episodesFilterState
            )
        }
    }

    override fun onRetryButtonPressed() {
        loadEpisodes(page = pageToLoadNumber, episodesFilterState)
    }

    override fun onQueryTextChange(text: String) {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        episodesFilterState.name = text
        loadEpisodes(page = pageToLoadNumber, episodesFilterState)
    }

    override fun onFilterStateChange(
        episodeName: String
    ) {
        episodesFilterState.episodeName = episodeName
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadEpisodes(page = pageToLoadNumber, episodesFilterState)
    }

    override fun onRefresh() {
        lastLoadedPageNumber = 0
        pageToLoadNumber = 1
        loadEpisodes(page = pageToLoadNumber, episodesFilterState)
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
        loadEpisodes(page = pageToLoadNumber, episodesFilterState)
    }

    private fun loadEpisodes(
        page: Int? = null,
        episodesFilterState: EpisodesFilterState
    ) {
        isLoading = true
        isEmptyResponse = false
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getEpisodesPage(
                page,
                episodesFilterState.name,
                episodesFilterState.episodeName
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    episodesPage = it
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
                        episodesPage = EntityPage(PageInfo(-1, -1, null, null), emptyList())
                    } else {
                        errorMode = true
                        loadFromRoomRepo()
                    }
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getEpisodesRepo().getAll(
            Constants.ENTITY_PAGE_SIZE, Constants.ENTITY_PAGE_SIZE * lastLoadedPageNumber,
            episodesFilterState.name,
            episodesFilterState.episodeName
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    episodesPage = EntityPage(PageInfo(-1, -1, "-1", prevPage), it)
                    if (it.isEmpty()) isEmptyResponse = true
                }
            )
    }

    private fun saveToLocalRepo(it: EntityPage<EpisodeEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getEpisodesRepo().addAll(it.results).subscribe()
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