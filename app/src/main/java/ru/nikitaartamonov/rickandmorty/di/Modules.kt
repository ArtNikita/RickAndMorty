package ru.nikitaartamonov.rickandmorty.di

import android.content.Context
import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.nikitaartamonov.rickandmorty.data.retrofit.RetrofitApi
import ru.nikitaartamonov.rickandmorty.data.room.CharacterDao
import ru.nikitaartamonov.rickandmorty.data.room.DataBase
import ru.nikitaartamonov.rickandmorty.data.room.EpisodeDao
import ru.nikitaartamonov.rickandmorty.data.room.LocationDao
import ru.nikitaartamonov.rickandmorty.domain.repos.CharactersRepo
import ru.nikitaartamonov.rickandmorty.domain.repos.EpisodesRepo
import ru.nikitaartamonov.rickandmorty.domain.repos.LocationsRepo
import ru.nikitaartamonov.rickandmorty.impl.CharactersRepoRoom
import ru.nikitaartamonov.rickandmorty.impl.EpisodesRepoRoom
import ru.nikitaartamonov.rickandmorty.impl.LocationsRepoRoom
import javax.inject.Singleton

@Module
class DbModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideCharactersRepo(characterDao: CharacterDao): CharactersRepo =
        CharactersRepoRoom(characterDao)

    @Provides
    @Singleton
    fun provideCharacterDao(dataBase: DataBase): CharacterDao = dataBase.characterDao()

    @Provides
    @Singleton
    fun provideDb(context: Context): DataBase =
        Room.databaseBuilder(context, DataBase::class.java, "rick_and_morty.db").build()

    @Provides
    @Singleton
    fun provideEpisodesRepo(episodeDao: EpisodeDao): EpisodesRepo =
        EpisodesRepoRoom(episodeDao)

    @Provides
    @Singleton
    fun provideEpisodeDao(dataBase: DataBase): EpisodeDao = dataBase.episodesDao()

    @Provides
    @Singleton
    fun provideLocationsRepo(locationDao: LocationDao): LocationsRepo =
        LocationsRepoRoom(locationDao)

    @Provides
    @Singleton
    fun provideLocationDao(dataBase: DataBase): LocationDao = dataBase.locationsDao()
}

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitApi(): RetrofitApi {
        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create(RetrofitApi::class.java)
    }
}

@Singleton
@Component(modules = [DbModule::class, NetworkModule::class])
interface AppComponent {
    fun getCharactersRepo(): CharactersRepo
    fun getEpisodesRepo(): EpisodesRepo
    fun getLocationRepo(): LocationsRepo
    fun getNetworkApi(): RetrofitApi
}
