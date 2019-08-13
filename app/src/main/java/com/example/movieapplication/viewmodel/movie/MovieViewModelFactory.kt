package com.example.movieapplication.viewmodel.movie

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class MovieViewModelFactory
@Inject constructor(private val movieViewModel: MovieViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return movieViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}