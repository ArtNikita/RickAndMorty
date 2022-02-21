package ru.nikitaartamonov.rickandmorty.domain.entities.character

data class CharactersFilterState(
    var name: String = "",
    var species: String? = "",
    var status: String? = "",
    var gender: String? = "",
) {
    companion object {
        enum class FilterType {
            SPECIES, STATUS, GENDER
        }
    }
}
