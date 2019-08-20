package com.example.movieapplication

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.res.Configuration
import android.support.v4.app.Fragment
import com.example.movieapplication.di.component.DaggerAppComponent
import com.example.movieapplication.di.module.AppModule
import com.example.movieapplication.di.module.NetModule
import com.example.movieapplication.di.module.StorageModule
import com.example.movieapplication.utils.Preferences
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .storageModule(StorageModule(applicationContext))
            .appModule(AppModule(this))
            .netModule(NetModule(BuildConfig.URL))
            .build()
            .inject(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        Preferences.setLocale(applicationContext)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector
}