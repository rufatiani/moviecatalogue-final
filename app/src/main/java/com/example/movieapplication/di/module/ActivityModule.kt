package com.example.movieapplication.di.module

import com.example.movieapplication.view.activity.HomeActivity
import com.example.movieapplication.view.activity.MovieDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailActivity(): MovieDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity
}