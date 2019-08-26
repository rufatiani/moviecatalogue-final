package com.example.movieapplication.data.dao

import android.arch.persistence.room.*
import com.example.movieapplication.data.model.TvShow
import io.reactivex.Single

@Dao
interface TvShowDao {

    @Query("SELECT * FROM tv_shows")
    fun queryTvShows(): Single<List<TvShow>>

    @Query("SELECT * FROM tv_shows WHERE name like :name")
    fun findTvShows(name: String): Single<List<TvShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShow(tvShow: TvShow): Long

    @Update
    fun updateTvShow(tvShow: TvShow)

    @Delete
    fun deleteTvShow(tvShow: TvShow)
}