package com.example.movieapplication.data.repository

import android.content.Context
import android.os.AsyncTask
import com.example.movieapplication.data.api.MovieApiInterface
import com.example.movieapplication.data.dao.MovieDao
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.model.PageMovie
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.LanguageManager
import io.reactivex.Observable
import javax.inject.Inject

class MovieRepository
@Inject constructor(
    private val movieApiInterface: MovieApiInterface,
    private val movieDao: MovieDao,
    private val context: Context
) {
    fun getMovies(): Observable<PageMovie> {
        return movieApiInterface.getMovies(Const.KEY_API, LanguageManager.getLanguage(context))
    }

    fun getMoviesFav(): Observable<List<Movie>> {
        return movieDao.queryMovies()
            .toObservable()
            .doOnNext {}
    }

    fun saveMovie(movie: Movie) {
        InsertMovieAsyncTask(movieDao).execute(movie)
    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieAsyncTask(movieDao).execute(movie)
    }

    private class InsertMovieAsyncTask(movieDao: MovieDao) : AsyncTask<Movie, Unit, Long>() {
        val movieDao = movieDao
        override fun doInBackground(vararg p0: Movie?): Long? {
            return movieDao.insertMovie(p0[0]!!)
        }
    }

    private class DeleteMovieAsyncTask(movieDao: MovieDao) : AsyncTask<Movie, Unit, Unit>() {
        val movieDao = movieDao
        override fun doInBackground(vararg p0: Movie?): Unit? {
            return movieDao.deleteMovie(p0[0]!!)
        }
    }
}