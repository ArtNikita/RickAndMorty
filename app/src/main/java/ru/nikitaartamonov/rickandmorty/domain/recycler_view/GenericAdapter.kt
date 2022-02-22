package ru.nikitaartamonov.rickandmorty.domain.recycler_view

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage

abstract class GenericAdapter<T : IdentifiedEntity, E : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<E>() {

    protected var entitiesList: MutableList<T> = mutableListOf()

    fun updateList(page: EntityPage<T>) {
        if (page.info.prev == null) {
            renderFirstPage(page)
        } else {
            renderAddedPage(page)
        }
    }

    private fun renderFirstPage(page: EntityPage<T>) {
        val diffUtil = EntitiesDiffUtil(entitiesList, ArrayList(page.results))
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        entitiesList = ArrayList(page.results)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun renderAddedPage(page: EntityPage<T>) {
        if (pageAlreadyAdded(page)) return
        val currentListSize = itemCount
        entitiesList.addAll(page.results)
        notifyItemRangeInserted(currentListSize, page.results.size)
    }

    private fun pageAlreadyAdded(page: EntityPage<T>): Boolean =
        page.results.isEmpty() || entitiesList.last().id == page.results.last().id


    override fun getItemCount(): Int = entitiesList.size

    private class EntitiesDiffUtil<K : IdentifiedEntity>(
        private val oldList: MutableList<K>,
        private val newList: MutableList<K>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

}