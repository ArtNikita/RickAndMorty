package ru.nikitaartamonov.rickandmorty.di

import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Module
class DbModule {
    //TODO: provide DataBase
}

@Singleton
@Component(modules = [DbModule::class])
interface AppComponent {
    //TODO: implement inject methods
}
