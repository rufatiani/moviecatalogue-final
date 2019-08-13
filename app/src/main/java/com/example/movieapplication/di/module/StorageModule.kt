package com.example.movieapplication.di.module

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun providesSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}