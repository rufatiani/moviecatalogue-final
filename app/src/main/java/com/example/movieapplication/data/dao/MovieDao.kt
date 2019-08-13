package com.example.movieapplication.data.dao

import android.arch.persistence.room.*
import com.example.movieapplication.data.model.Movie
import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun queryMovies(): Single<List<Movie>>

    @Query("SELECT * FROM movies WHERE title LIKE :title")
    fun findMovies(title : String): Single<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie): Long

    @Update
    fun updateMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)
}