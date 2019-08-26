package com.example.movieapplication.view.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.movieapplication.R
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.repository.MovieRepository
import com.example.movieapplication.data.service.task.DownloadImageTask
import com.example.movieapplication.utils.Const

class FavoriteWidgetRemoteViewsFactory(val context: Context, val movieRepository: MovieRepository) :
    RemoteViewsService.RemoteViewsFactory {

    private var items: MutableList<Bitmap> = ArrayList()
    private var movies: List<Movie> = ArrayList()

    override fun onCreate() {
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        movies = movieRepository.getMoviesFavSync()
        setBitmap(movies)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_widget)
        remoteViews.setImageViewBitmap(R.id.ivItemWidget, items[position])

        val bundle = Bundle()
        bundle.putInt(FavoriteWidget.EXTRA_ITEM, position)

        val fillIntent = Intent()
        fillIntent.putExtras(bundle)

        remoteViews.setOnClickFillInIntent(R.id.ivItemWidget, fillIntent)
        return remoteViews
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

    private fun setBitmap(list: List<Movie>) {
        for (i in 0 until list.size) {
            items.add(DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + list[i].image).get())
        }
    }
}