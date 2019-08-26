package com.example.movieapplication.viewmodel.tvshow

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.data.repository.TvShowRepository
import com.example.movieapplication.data.service.task.DownloadImageTask
import com.example.movieapplication.utils.Const
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TvShowFavoriteViewModel
@Inject constructor(private val tvShowRepository: TvShowRepository) : ViewModel() {
    private var tvs = MutableLiveData<HashMap<String, Any>>()
    private var error: Boolean = false

    init {
        loadTvs()
    }

    fun tvsResult(): MutableLiveData<HashMap<String, Any>> {
        return tvs
    }

    fun isError(): Boolean {
        return error
    }

    fun loadTvs() {
        val disposableObserver = object : DisposableObserver<List<TvShow>>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(list: List<TvShow>) {
                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_TV] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                tvs.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        tvShowRepository.getTvsFav()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun searchTvs(name: String) {
        val disposableObserver = object : DisposableObserver<List<TvShow>>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(list: List<TvShow>) {
                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_TV] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                tvs.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        tvShowRepository.findTvsFav(name)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun savetvShow(tvShow: TvShow) {
        tvShowRepository.saveTvShow(tvShow)
    }

    fun deleteTvShow(tvShow: TvShow) {
        tvShowRepository.deleteTvShow(tvShow)
    }

    private fun setBitmap(list: List<TvShow>): List<Bitmap?> {
        val listBitmap = ArrayList<Bitmap?>()
        for (i in 0 until list.size) {
            listBitmap.add(DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + list[i].image).get())
        }

        return listBitmap
    }
}