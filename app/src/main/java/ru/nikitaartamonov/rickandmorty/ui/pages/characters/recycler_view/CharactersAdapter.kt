package ru.nikitaartamonov.rickandmorty.ui.pages.characters.recycler_view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity

class CharactersAdapter : RecyclerView.Adapter<CharactersViewHolder>() {

    var charactersList: MutableList<CharacterEntity> = mutableListOf()

    fun updateList(page: EntityPage<CharacterEntity>) {
        if (page.info.prev == null) {
            renderFirstPage(page)
        } else {
            renderAddedPage(page)
        }
    }

    private fun renderFirstPage(page: EntityPage<CharacterEntity>) {
        val diffUtil = CharactersDiffUtil(charactersList, ArrayList(page.results))
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        charactersList = ArrayList(page.results)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun renderAddedPage(page: EntityPage<CharacterEntity>) {
        if (pageAlreadyAdded(page)) return
        val currentListSize = itemCount
        charactersList.addAll(page.results)
        notifyItemRangeInserted(currentListSize, page.results.size)
    }

    private fun pageAlreadyAdded(page: EntityPage<CharacterEntity>): Boolean =
        page.results.isEmpty() || charactersList.last().id == page.results.last().id


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(charactersList[position])
    }

    override fun getItemCount(): Int = charactersList.size

    private class CharactersDiffUtil(
        private val oldList: MutableList<CharacterEntity>,
        private val newList: MutableList<CharacterEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

}