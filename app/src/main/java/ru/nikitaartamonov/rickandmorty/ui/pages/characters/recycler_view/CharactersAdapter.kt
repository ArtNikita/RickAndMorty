package ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

class CharactersAdapter : RecyclerView.Adapter<CharactersViewHolder>() {

    var charactersList: List<CharacterEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(charactersList[position])
    }

    override fun getItemCount(): Int = charactersList.size

}