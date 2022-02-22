package ru.nikitaartamonov.rickandmorty.domain.entities.location

data class LocationsFilterState(
    var name: String = "",
    var type: String? = "",
    var dimension: String? = ""
) {
    companion object {
        enum class FilterType {
            TYPE, DIMENSION
        }
    }
}