package ru.nikitaartamonov.rickandmorty.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.nikitaartamonov.rickandmorty.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}