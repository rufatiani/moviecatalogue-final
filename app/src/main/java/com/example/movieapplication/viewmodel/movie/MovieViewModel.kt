package com.example.movieapplication.viewmodel.movie

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.model.PageMovie
import com.example.movieapplication.data.repository.MovieRepository
import com.example.movieapplication.data.service.task.DownloadImageTask
import com.example.movieapplication.utils.Const
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieViewModel
@Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private var movieMap = MutableLiveData<HashMap<String, Any>>()
    private var error: Boolean = false

    init {
        loadMovies()
    }

    fun moviesResult(): MutableLiveData<HashMap<String, Any>> {
        return movieMap
    }

    fun isError(): Boolean {
        return error
    }

    fun loadMovies() {
        val disposableObserver = object : DisposableObserver<PageMovie>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(pageMovie: PageMovie) {
                val list: List<Movie> = pageMovie.results

                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_MOVIE] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                movieMap.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        movieRepository.getMovies()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun searchMovies(query : String) {
        val disposableObserver = object : DisposableObserver<PageMovie>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(pageMovie: PageMovie) {
                val list: List<Movie> = pageMovie.results

                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_MOVIE] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                movieMap.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        movieRepository.findMovies(query)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    private fun setBitmap(list: List<Movie>): List<Bitmap?> {
        val listBitmap = ArrayList<Bitmap?>()
        for (i in 0 until list.size) {
            listBitmap.add(DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + list[i].image).get())
        }

        return listBitmap
    }
}