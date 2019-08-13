package com.example.movieapplication.di.module

import com.example.movieapplication.data.api.MovieApiInterface
import com.example.movieapplication.data.api.TvShowApiInterface
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetModule(private val baseUrl: String) {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesMovieApiInterface(retrofit: Retrofit): MovieApiInterface = retrofit.create(
        MovieApiInterface::class.java
    )

    @Provides
    @Singleton
    fun providesTvShowApiInterface(retrofit: Retrofit): TvShowApiInterface = retrofit.create(
        TvShowApiInterface::class.java
    )
}