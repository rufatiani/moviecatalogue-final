package com.example.movieapplication.di.component

import com.example.movieapplication.MainApplication
import com.example.movieapplication.di.module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        AppModule::class,
        NetModule::class,
        StorageModule::class,
        ServiceModule::class,
        ProviderModule::class]
)

interface AppComponent {
    fun inject(app: MainApplication)
}
