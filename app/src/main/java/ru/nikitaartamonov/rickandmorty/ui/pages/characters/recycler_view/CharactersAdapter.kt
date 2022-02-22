package ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view

import android.view.ViewGroup
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.GenericAdapter

class CharactersAdapter : GenericAdapter<CharacterEntity, CharactersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}