package com.example.movieapplication.di.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import com.example.movieapplication.data.dao.MovieDao
import com.example.movieapplication.data.dao.MovieDatabase
import com.example.movieapplication.data.dao.TvShowDao
import com.example.movieapplication.data.dao.TvShowDatabase
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModelFactory
import com.example.movieapplication.viewmodel.movie.MovieViewModelFactory
import com.example.movieapplication.viewmodel.tvshow.TvShowFavoriteViewModelFactory
import com.example.movieapplication.viewmodel.tvshow.TvShowViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    fun provideMovieViewModelFactory(
        factory: MovieViewModelFactory
    ): ViewModelProvider.Factory = factory

    @Provides
    fun provideMovieFavoriteViewModelFactory(
        factory: MovieFavoriteViewModelFactory
    ): ViewModelProvider.Factory = factory

    @Provides
    fun provideTvShowViewModelFactory(
        factory: TvShowViewModelFactory
    ): ViewModelProvider.Factory = factory

    @Provides
    fun provideTvShowFavoriteViewModelFactory(
        factory: TvShowFavoriteViewModelFactory
    ): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun provideMovieDatabase(app: Application): MovieDatabase = Room.databaseBuilder(
        app,
        MovieDatabase::class.java, "movie_db"
    ).build()

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

    @Provides
    @Singleton
    fun provideTvShowDatabase(app: Application): TvShowDatabase = Room.databaseBuilder(
        app,
        TvShowDatabase::class.java, "tv_db"
    ).build()

    @Provides
    @Singleton
    fun provideTvShowDao(database: TvShowDatabase): TvShowDao = database.tvshowDao()
}