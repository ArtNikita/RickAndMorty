package ru.nikitaartamonov.rickandmorty.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.nikitaartamonov.rickandmorty.domain.entities.location.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = REPLACE)
    fun add(location: LocationEntity): Single<Long>

    @Insert(onConflict = REPLACE)
    fun addAll(locations: List<LocationEntity>): Completable

    @Query("SELECT * FROM locationentity WHERE name LIKE '%' || :name || '%' AND type LIKE '%' || :type || '%' AND dimension LIKE '%' || :dimension || '%' LIMIT :limit OFFSET :offset")
    fun getAll(
        limit: Int,
        offset: Int,
        name: String,
        type: String,
        dimension: String
    ): Single<List<LocationEntity>>

    @Query("SELECT * FROM locationentity WHERE id == :id")
    fun getById(id: Int): Single<LocationEntity>

    @Query("SELECT * FROM locationentity WHERE id in (:ids)")
    fun getByIds(ids: List<Int>): Single<List<LocationEntity>>
}