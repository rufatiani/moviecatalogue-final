package com.example.movieapplication.viewmodel.tvshow

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class TvShowFavoriteViewModelFactory
@Inject constructor(private val tvShowFavoriteViewModel: TvShowFavoriteViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvShowFavoriteViewModel::class.java)) {
            return tvShowFavoriteViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}