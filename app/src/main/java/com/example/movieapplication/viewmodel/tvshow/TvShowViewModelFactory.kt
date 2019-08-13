package com.example.movieapplication.viewmodel.tvshow

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class TvShowViewModelFactory
@Inject constructor(private val tvShowViewModel: TvShowViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvShowViewModel::class.java)) {
            return tvShowViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}