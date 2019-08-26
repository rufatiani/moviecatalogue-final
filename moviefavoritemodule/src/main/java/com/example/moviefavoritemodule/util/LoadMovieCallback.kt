package com.example.moviefavoritemodule.util

import android.database.Cursor

internal interface LoadMovieCallback {
    fun postExecute(cursor: Cursor?)
}