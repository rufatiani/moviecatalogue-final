package com.example.movieapplication.di.module

import com.example.movieapplication.data.provider.MovieProvider
import com.example.movieapplication.data.provider.TvShowProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProviderModule {
    @ContributesAndroidInjector
    abstract fun contributeMovieProvider(): MovieProvider

    @ContributesAndroidInjector
    abstract fun contributeTvProvider(): TvShowProvider
}