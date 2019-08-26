package com.example.movieapplication.data.repository

import android.content.Context
import android.os.AsyncTask
import com.example.movieapplication.data.api.TvShowApiInterface
import com.example.movieapplication.data.dao.TvShowDao
import com.example.movieapplication.data.model.PageTvShow
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.Preferences
import io.reactivex.Observable
import javax.inject.Inject

class TvShowRepository
@Inject constructor(
    private val tvShowApiInterface: TvShowApiInterface,
    private val tvShowDao: TvShowDao,
    private val context: Context
) {
    fun getTvs(): Observable<PageTvShow> {
        return tvShowApiInterface.getTvs(Const.KEY_API, Preferences.getLanguage(context))
    }

    fun findTvs(query: String): Observable<PageTvShow> {
        return tvShowApiInterface.findTvs(Const.KEY_API, Preferences.getLanguage(context), query)
    }

    fun getTvsFav(): Observable<List<TvShow>> {
        return tvShowDao.queryTvShows()
            .toObservable()
            .doOnNext {}
    }

    fun findTvsFav(name: String): Observable<List<TvShow>> {
        return tvShowDao.findTvShows("%" + name + "%")
            .toObservable()
            .doOnNext {}
    }

    fun saveTvShow(tvShow: TvShow) {
        InsertTvShowAsyncTask(tvShowDao).execute(tvShow)
    }

    fun deleteTvShow(tvShow: TvShow) {
        DeleteTvShowAsyncTask(tvShowDao).execute(tvShow)
    }

    private class InsertTvShowAsyncTask(tvShowDao: TvShowDao) : AsyncTask<TvShow, Unit, Long>() {
        val tvShowDao = tvShowDao
        override fun doInBackground(vararg p0: TvShow?): Long? {
            return tvShowDao.insertTvShow(p0[0]!!)
        }
    }

    private class DeleteTvShowAsyncTask(tvShowDao: TvShowDao) : AsyncTask<TvShow, Unit, Unit>() {
        val tvShowDao = tvShowDao
        override fun doInBackground(vararg p0: TvShow?): Unit? {
            return tvShowDao.deleteTvShow(p0[0]!!)
        }
    }

}