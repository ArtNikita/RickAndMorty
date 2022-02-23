package ru.nikitaartamonov.rickandmorty.domain.recycler_view

interface OnItemClickListener {

    fun <T : IdentifiedEntity> onClick(entity: T)
}