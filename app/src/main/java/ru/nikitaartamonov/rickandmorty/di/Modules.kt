package ru.nikitaartamonov.rickandmorty.di

import android.content.Context
import androidx.room.Room
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.nikitaartamonov.rickandmorty.data.room.CharacterDao
import ru.nikitaartamonov.rickandmorty.data.room.DataBase
import ru.nikitaartamonov.rickandmorty.domain.repos.CharactersRepo
import ru.nikitaartamonov.rickandmorty.impl.CharactersRepoRoom
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
    fun provideDb(context: Context, dbPath: String) : DataBase =
        Room.databaseBuilder(context, DataBase::class.java, dbPath).build()

    @Provides
    @Singleton
    fun provideDbPath() : String = "rick_and_morty.db"
}

@Singleton
@Component(modules = [DbModule::class])
interface AppComponent {
    fun getCharactersRepo(): CharactersRepo
}
