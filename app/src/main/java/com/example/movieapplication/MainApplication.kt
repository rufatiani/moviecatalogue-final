package com.example.movieapplication

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.support.v4.app.Fragment
import com.example.movieapplication.di.component.DaggerAppComponent
import com.example.movieapplication.di.module.AppModule
import com.example.movieapplication.di.module.NetModule
import com.example.movieapplication.di.module.StorageModule
import com.example.movieapplication.utils.LanguageManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

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
        LanguageManager.setLocale(applicationContext)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}