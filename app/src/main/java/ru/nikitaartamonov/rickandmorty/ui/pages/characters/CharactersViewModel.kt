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
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

class CharactersViewModel(application: Application) : AndroidViewModel(application),
    CharactersContract.ViewModel {

    override val showLoadingIndicatorLiveData: LiveData<Boolean> = MutableLiveData()
    override val renderCharactersListLiveData: LiveData<List<CharacterEntity>> = MutableLiveData()

    private var charactersRepo: List<CharacterEntity> = emptyList()
        set(value) {
            field = value
            renderCharactersListLiveData.postValue(value)
        }

    override fun onViewCreated() {
        //todo load and render
    }

    private var isLoading = false
        set(value) {
            field = value
            showLoadingIndicatorLiveData.postValue(value)
        }

    private var compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(getApplication<App>().appComponent.getNetworkApi()
            .getCharacterPage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    charactersRepo = it.results
                    //todo add to room repo
                },
                onError = {
                    //todo load from room repo, show error message
                    Log.i("@@@", it.message.toString())
                }
            )
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