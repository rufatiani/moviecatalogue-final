package com.example.movieapplication.data.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.movieapplication.data.model.Movie

@Database(entities = arrayOf(Movie::class), version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}