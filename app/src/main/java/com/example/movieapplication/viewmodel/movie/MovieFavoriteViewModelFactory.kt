package com.example.movieapplication.viewmodel.movie

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class MovieFavoriteViewModelFactory
@Inject constructor(private val movieFavoriteViewModel: MovieFavoriteViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieFavoriteViewModel::class.java)) {
            return movieFavoriteViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}