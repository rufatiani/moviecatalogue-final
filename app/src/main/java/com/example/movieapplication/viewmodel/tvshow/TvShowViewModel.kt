package com.example.movieapplication.viewmodel.tvshow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.example.movieapplication.data.model.PageTvShow
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.data.repository.TvShowRepository
import com.example.movieapplication.data.service.task.DownloadImageTask
import com.example.movieapplication.utils.Const
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TvShowViewModel
@Inject constructor(private val tvShowRepository: TvShowRepository) : ViewModel() {

    private var tvMap = MutableLiveData<HashMap<String, Any>>()
    private var error: Boolean = false

    init {
        loadTVs()
    }

    fun isError(): Boolean {
        return error
    }

    fun tvsResult(): LiveData<HashMap<String, Any>> {
        return tvMap
    }

    fun loadTVs() {
        val disposableObserver = object : DisposableObserver<PageTvShow>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(pageTvShow: PageTvShow) {
                val list: List<TvShow> = pageTvShow.results

                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_TV] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                tvMap.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        tvShowRepository.getTvs()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun searchTVs(query : String) {
        val disposableObserver = object : DisposableObserver<PageTvShow>() {
            override fun onComplete() {
                error = false
            }

            override fun onNext(pageTvShow: PageTvShow) {
                val list: List<TvShow> = pageTvShow.results

                val map = HashMap<String, Any>()
                map[Const.PARCEL_KEY_TV] = list
                map[Const.PARCEL_KEY_BITMAP] = setBitmap(list)

                tvMap.postValue(map)
            }

            override fun onError(e: Throwable) {
                error = true
            }
        }

        tvShowRepository.findTvs(query)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    private fun setBitmap(list: List<TvShow>): List<Bitmap?> {
        val listBitmap = ArrayList<Bitmap?>()
        for (i in 0 until list.size) {
            listBitmap.add(DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + list[i].image).get())
        }

        return listBitmap
    }
}