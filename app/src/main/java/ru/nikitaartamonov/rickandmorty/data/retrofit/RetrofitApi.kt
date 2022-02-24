package ru.nikitaartamonov.rickandmorty.data.retrofit

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nikitaartamonov.rickandmorty.domain.entities.EntityPage
import ru.nikitaartamonov.rickandmorty.domain.entities.character.CharacterEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity

interface RetrofitApi {

    @GET("character")
    fun getCharacterPage(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("gender") gender: String? = null
    ): Single<EntityPage<CharacterEntity>>

    @GET("episode")
    fun getEpisodesPage(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("episode") episode: String? = null,
    ): Single<EntityPage<EpisodeEntity>>

    @GET("location")
    fun getLocationsPage(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("dimension") dimension: String? = null,
    ): Single<EntityPage<LocationEntity>>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Single<CharacterEntity>

    @GET("character/{ids}")
    fun getCharactersByIds(@Path("ids") ids: String): Single<List<CharacterEntity>>

    @GET("episode/{id}")
    fun getEpisodeById(@Path("id") id: String): Single<EpisodeEntity>

    @GET("episode/{ids}")
    fun getEpisodesByIds(@Path("ids") ids: String): Single<List<EpisodeEntity>>

    @GET("location/{id}")
    fun getLocationById(@Path("id") id: String): Single<LocationEntity>

    @GET("location/{ids}")
    fun getLocationsByIds(@Path("ids") ids: String): Single<List<LocationEntity>>
}