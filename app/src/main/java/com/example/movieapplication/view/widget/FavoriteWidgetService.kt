package com.example.movieapplication.view.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.movieapplication.data.repository.MovieRepository
import dagger.android.AndroidInjection
import javax.inject.Inject

class FavoriteWidgetService : RemoteViewsService() {

    @Inject
    lateinit var movieRepository : MovieRepository

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        AndroidInjection.inject(this);
        return FavoriteWidgetRemoteViewsFactory(this.applicationContext, movieRepository)
    }

}