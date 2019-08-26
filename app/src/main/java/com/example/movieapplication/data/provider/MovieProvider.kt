package com.example.movieapplication.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.repository.MovieRepository
import dagger.android.AndroidInjection
import javax.inject.Inject

class MovieProvider : ContentProvider() {

    @Inject
    lateinit var movieRepository: MovieRepository
    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val authority = "com.example.movieapplication.movie"
    private val movieId = 1

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
        if (uriMatcher.match(uri) == movieId) {
            val columns =
                arrayOf("id", "title", "original_language", "release_date", "overview", "vote_average", "poster_path")
            val matrixCursor = MatrixCursor(columns)
            var movies: List<Movie>? = ArrayList()
            movieRepository.getMoviesFav().subscribe() {
                movies = it
            }

            for (movie in movies!!) {
                matrixCursor.addRow(
                    arrayOf(
                        movie.id,
                        movie.title,
                        movie.language,
                        movie.releaseDate,
                        movie.overview,
                        movie.rating,
                        movie.image
                    )
                )
            }

            return matrixCursor
        }

        return null
    }

    override fun onCreate(): Boolean {
        AndroidInjection.inject(this)
        uriMatcher.addURI(authority, "movie", movieId)
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