package ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view

import android.view.ViewGroup
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.GenericAdapter
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener

class CharactersAdapter(listener: OnItemClickListener) :
    GenericAdapter<CharacterEntity, CharactersViewHolder>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(entitiesList[position])
    }
}