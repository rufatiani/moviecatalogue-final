package com.example.movieapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.movieapplication.data.dao.MovieDao
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.repository.MovieRepository
import com.example.movieapplication.view.FavoriteWidget

class FavoriteWidgetRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory{

    private var items : MutableList<Bitmap> = ArrayList()
    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        items.add(BitmapFactory.decodeResource(context.resources, R.drawable.example_appwidget_preview))
        items.add(BitmapFactory.decodeResource(context.resources, R.drawable.example_appwidget_preview))
        items.add(BitmapFactory.decodeResource(context.resources, R.drawable.example_appwidget_preview))
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

    private fun getBitmap(){

    }
}