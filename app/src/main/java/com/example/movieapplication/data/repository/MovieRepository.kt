package com.example.movieapplication.data.repository

import android.content.Context
import android.os.AsyncTask
import com.example.movieapplication.data.api.MovieApiInterface
import com.example.movieapplication.data.dao.MovieDao
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.model.PageMovie
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.Preferences
import io.reactivex.Observable
import javax.inject.Inject

class MovieRepository
@Inject constructor(
    private val movieApiInterface: MovieApiInterface,
    private val movieDao: MovieDao,
    private val context: Context
) {
    fun getMovies(): Observable<PageMovie> {
        return movieApiInterface.getMovies(Const.KEY_API, Preferences.getLanguage(context))
    }

    fun findMovies(query : String): Observable<PageMovie> {
        return movieApiInterface.findMovies(Const.KEY_API, Preferences.getLanguage(context), query)
    }

    fun getMoviesFav(): Observable<List<Movie>> {
        return movieDao.queryMovies()
            .toObservable()
            .doOnNext {}
    }

    fun getMoviesFavSync(): List<Movie> {
        return movieDao.queryMoviesSync()
    }

    fun findMoviesFav(title : String): Observable<List<Movie>> {
        return movieDao.findMovies("%"+title+"%")
            .toObservable()
            .doOnNext {}
    }

    fun saveMovie(movie: Movie) {
        InsertMovieAsyncTask(movieDao).execute(movie)
    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieAsyncTask(movieDao).execute(movie)
    }

    private class InsertMovieAsyncTask(val movieDao: MovieDao) : AsyncTask<Movie, Unit, Long>() {
        override fun doInBackground(vararg p0: Movie?): Long? {
            return movieDao.insertMovie(p0[0]!!)
        }
    }

    private class DeleteMovieAsyncTask(val movieDao: MovieDao) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg p0: Movie?): Unit? {
            return movieDao.deleteMovie(p0[0]!!)
        }
    }
}