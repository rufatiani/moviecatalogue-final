package com.example.movieapplication.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.data.repository.TvShowRepository
import dagger.android.AndroidInjection
import javax.inject.Inject

class TvShowProvider : ContentProvider() {
    @Inject
    lateinit var tvShowRepository: TvShowRepository
    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val authority = "com.example.movieapplication.tvshow"
    private val tvId = 2

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) == tvId) {
            val columns =
                arrayOf("id", "name", "original_language", "first_air_date", "overview", "vote_average", "poster_path")
            val matrixCursor = MatrixCursor(columns)
            var tvs: List<TvShow>? = ArrayList()
            tvShowRepository.getTvsFav().subscribe() {
                tvs = it
            }

            for (tv in tvs!!) {
                matrixCursor.addRow(
                    arrayOf(
                        tv.id,
                        tv.name,
                        tv.language,
                        tv.firstAirDate,
                        tv.overview,
                        tv.rating,
                        tv.image
                    )
                )
            }

            return matrixCursor
        }

        return null
    }

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)
        uriMatcher.addURI(authority, "tvshow", tvId)
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}