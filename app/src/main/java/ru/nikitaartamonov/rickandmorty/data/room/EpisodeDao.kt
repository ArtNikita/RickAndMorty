package ru.nikitaartamonov.rickandmorty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.nikitaartamonov.rickandmorty.domain.entities.episode.EpisodeEntity

@Dao
interface EpisodeDao {

    @Insert(onConflict = REPLACE)
    fun add(episode: EpisodeEntity): Single<Long>

    @Insert(onConflict = REPLACE)
    fun addAll(episodes: List<EpisodeEntity>): Completable

    @Query("SELECT * FROM episodeentity WHERE name LIKE '%' || :name || '%' AND episode LIKE '%' || :episode || '%' LIMIT :limit OFFSET :offset")
    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        episode: String
    ): Single<List<EpisodeEntity>>

    @Query("SELECT * FROM episodeentity WHERE id == :id")
    fun getById(id: Int): Single<EpisodeEntity>

    @Query("SELECT * FROM episodeentity WHERE id in (:ids)")
    fun getByIds(ids: List<Int>): Single<List<EpisodeEntity>>
}