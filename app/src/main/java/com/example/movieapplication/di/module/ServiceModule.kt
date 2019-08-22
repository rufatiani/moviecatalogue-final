package com.example.movieapplication.di.module

import com.example.movieapplication.data.service.DailyAlarmReceiver
import com.example.movieapplication.view.widget.FavoriteWidgetService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule{
    @ContributesAndroidInjector
    abstract fun contributeFavoriteWidgetService(): FavoriteWidgetService

    @ContributesAndroidInjector
    abstract fun contributeDailyAlarmReceiver(): DailyAlarmReceiver
}