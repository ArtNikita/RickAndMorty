package ru.nikitaartamonov.rickandmorty.ui.pages.characters

import android.app.Application
import android.util.Log
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
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view.CharactersAdapter

class CharactersViewModel(application: Application) : AndroidViewModel(application),
    CharactersContract.ViewModel {

    override val adapter: CharactersAdapter = CharactersAdapter()

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderCharactersListLiveData: LiveData<EntityPage<CharacterEntity>> =
        MutableLiveData()

    private var lastLoadedPageNumber = 0
    private var pageToLoadNumber = 1

    private var charactersPage: EntityPage<CharacterEntity> =
        EntityPage(PageInfo(0, 0, null, null), emptyList())
        set(value) {
            field = value
            renderCharactersListLiveData.postValue(value)
        }

    override fun onRecyclerViewScrolledDown() {
        if (pageToLoadNumber == lastLoadedPageNumber) loadCharacters(page = ++pageToLoadNumber)
    }

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(value)
        }

    private var compositeDisposable = CompositeDisposable()

    init {
        loadCharacters(page = pageToLoadNumber)
    }

    private fun loadCharacters(
        page: Int? = null,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        isLoading = true
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getCharacterPage(page, name, status, species, gender)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    charactersPage = it
                    lastLoadedPageNumber = pageToLoadNumber
                    saveToLocalRepo(it)
                    isLoading = false
                },
                onError = {
                    //todo show error message
                    Log.i("@@@", it.message.toString())
                    loadFromRoomRepo()
                }
            )
        )
    }

    private fun loadFromRoomRepo() {
        getApplication<App>().appComponent.getCharactersRepo().getAll(
            Constants.ENTITY_PAGE_SIZE, Constants.ENTITY_PAGE_SIZE * lastLoadedPageNumber
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isLoading = false
                    val prevPage = if (lastLoadedPageNumber == 0) null else "-1"
                    charactersPage = EntityPage(PageInfo(-1, -1, "-1", prevPage), it)
                },
                onError = {
                    Log.i("@@@", it.message.toString())
                }
            )
    }

    private fun saveToLocalRepo(it: EntityPage<CharacterEntity>) {
        compositeDisposable.add(
            getApplication<App>().appComponent.getCharactersRepo().addAll(it.results).subscribe()
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