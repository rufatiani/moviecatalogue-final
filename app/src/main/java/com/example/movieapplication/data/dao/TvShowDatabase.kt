package com.example.movieapplication.data.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.movieapplication.data.model.TvShow

@Database(entities = arrayOf(TvShow::class), version = 1, exportSchema = false)
abstract class TvShowDatabase : RoomDatabase() {
    abstract fun tvshowDao(): TvShowDao
}