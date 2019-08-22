package com.example.movieapplication.di.module

import com.example.movieapplication.view.activity.HomeActivity
import com.example.movieapplication.view.activity.MovieDetailActivity
import com.example.movieapplication.view.activity.ReminderActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailActivity(): MovieDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun contributeReminderActivity(): ReminderActivity
}