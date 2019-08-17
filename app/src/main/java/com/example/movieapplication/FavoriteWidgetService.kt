package com.example.movieapplication

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return FavoriteWidgetRemoteViewsFactory(this.applicationContext)
    }

}