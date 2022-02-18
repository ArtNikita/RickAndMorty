package ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nikitaartamonov.rickandmorty.databinding.ItemCharacterBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

class CharactersViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    ItemCharacterBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ).root
) {

    private val binding = ItemCharacterBinding.bind(itemView)

    fun bind(characterEntity: CharacterEntity) {
        binding.itemCharacterNameTextView.text = characterEntity.name
        binding.itemCharacterSpeciesTextView.text = characterEntity.species
        binding.itemCharacterStatusTextView.text = characterEntity.status
        binding.itemCharacterGenderTextView.text = characterEntity.gender
        Glide
            .with(itemView.context)
            .load(characterEntity.image)
            .into(binding.characterItemImageView)
    }
}