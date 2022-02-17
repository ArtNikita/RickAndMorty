package ru.nikitaartamonov.rickandmorty

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import ru.nikitaartamonov.rickandmorty.di.AppComponent
import ru.nikitaartamonov.rickandmorty.di.DaggerAppComponent
import ru.nikitaartamonov.rickandmorty.di.DbModule

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .dbModule(DbModule(this))
            .build()
    }
}

val Context.app: App
    get() = applicationContext as App

val Fragment.app: App
    get() = requireActivity().app