package com.example.movieapplication.di.module

import com.example.movieapplication.view.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeMovieFragment(): MovieFragment

    @ContributesAndroidInjector
    abstract fun contributeTvFragment(): TVShowFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieFavoriteFragment(): MovieFavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeTvShowFavoriteFragment(): TvShowFavoriteFragment
}