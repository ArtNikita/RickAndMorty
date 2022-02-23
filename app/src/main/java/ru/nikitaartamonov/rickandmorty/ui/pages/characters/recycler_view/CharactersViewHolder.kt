package ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nikitaartamonov.rickandmorty.databinding.ItemCharacterBinding
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.recycler_view.OnItemClickListener

class CharactersViewHolder(parent: ViewGroup, listener: OnItemClickListener) :
    RecyclerView.ViewHolder(
        ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
    ) {

    init {
        itemView.setOnClickListener { listener.onClick(currentCharacter) }
    }

    private val binding = ItemCharacterBinding.bind(itemView)
    private lateinit var currentCharacter: CharacterEntity

    fun bind(characterEntity: CharacterEntity) {
        currentCharacter = characterEntity
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