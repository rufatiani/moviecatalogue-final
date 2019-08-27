package com.example.moviefavoritemodule.view.fragment

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviefavoritemodule.R
import com.example.moviefavoritemodule.data.adapter.TvShowAdapter
import com.example.moviefavoritemodule.data.model.TvShow
import com.example.moviefavoritemodule.util.Const
import com.example.moviefavoritemodule.util.DownloadImageTask
import com.example.moviefavoritemodule.util.LoadMovieCallback
import kotlinx.android.synthetic.main.fragment_movie.*
import java.lang.ref.WeakReference

class TvShowFragment : Fragment(), LoadMovieCallback {

    private lateinit var adapter: TvShowAdapter

    companion object {
        val CONTENT_URI: Uri = Uri.Builder().scheme("content")
            .authority("com.example.movieapplication.tvshow")
            .appendPath("tvshow")
            .build()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecycler()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)
        val dataObserver = context?.let {
            DataObserver(
                handler,
                it
            )
        }
        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, dataObserver)
        context?.let { LoadMovieTask(it, this).execute() }
    }

    override fun postExecute(cursor: Cursor?) {
        if (cursor != null) {
            setMovieAdapter(cursor)
        }
    }

    override fun onResume() {
        super.onResume()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)
        val dataObserver = context?.let {
            DataObserver(
                handler,
                it
            )
        }
        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, dataObserver)
        context?.let { LoadMovieTask(it, this).execute() }
    }

    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(activity, 1)
        gridLayoutManager.orientation = RecyclerView.VERTICAL
        rlMoviesFavorite.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
        }
    }

    private fun setMovieAdapter(cursor: Cursor) {
        pbMovieFavorite.visibility = View.VISIBLE
        val tvs = ArrayList<TvShow>()
        val bitmaps = ArrayList<Bitmap>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            tvs.add(
                TvShow(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getDouble(5),
                    cursor.getString(6)
                )
            )

            bitmaps.add(DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + cursor.getString(6)).get())
            cursor.moveToNext()
        }

        adapter = TvShowAdapter(tvs, bitmaps)
        rlMoviesFavorite.adapter = adapter
        pbMovieFavorite.visibility = View.GONE
    }

    private class LoadMovieTask(context: Context, callback: LoadMovieCallback) : AsyncTask<Void, Void, Cursor?>() {
        private val weakReferenceContext: WeakReference<Context> = WeakReference(context)
        private val weakReferenceCallback: WeakReference<LoadMovieCallback>? = WeakReference(callback)

        override fun doInBackground(vararg params: Void?): Cursor? {
            return weakReferenceContext
                .get()?.contentResolver?.query(
                CONTENT_URI,
                null,
                null,
                null,
                null
            )
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            weakReferenceCallback?.get()?.postExecute(result)
        }

    }

    private class DataObserver(handler: Handler, val context: Context) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            LoadMovieTask(
                context,
                context as TvShowFragment
            ).execute()
        }
    }
}
