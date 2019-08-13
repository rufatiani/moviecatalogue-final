package com.example.movieapplication.viewmodel.movie

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.repository.MovieRepository
import com.example.movieapplication.data.task.DownloadImageTask
import com.example.movieapplication.utils.Const
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieFavoriteViewModel
@Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {
    private var movies = MutableLiveData<HashMap<String, Any>>()
    private var error: Boolean = false

    init {
        loadMovies()
    }

    fun moviesResult(): MutableLiveData<HashMap<String, Any>> {
        return movies
    }

    fun isError(): Boolean {
        return error
    }

    fun loadMovies() {
        val disposableObserver = object : DisposableObserver<List<Movie>>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(list: List<Movie>) {
                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_MOVIE] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                movies.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        movieRepository.getMoviesFav()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun saveMovie(movie: Movie) {
        movieRepository.saveMovie(movie)
    }

    fun deleteMovie(movie: Movie) {
        movieRepository.deleteMovie(movie)
    }

    private fun setBitmap(list: List<Movie>): List<Bitmap?> {
        val listBitmap = ArrayList<Bitmap?>()
        for (i in 0 until list.size) {
            listBitmap.add(DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + list[i].image).get())
        }

        return listBitmap
    }
}