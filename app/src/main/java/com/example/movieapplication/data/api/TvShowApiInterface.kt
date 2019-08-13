package com.example.movieapplication.data.api

import com.example.movieapplication.data.model.PageTvShow
import com.example.movieapplication.utils.Const
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TvShowApiInterface {
    @GET("discover/tv")
    fun getTvs(@Query(Const.QUERY_API_KEY) apiKey: String, @Query(Const.QUERY_LANGUAGE) language: String): Observable<PageTvShow>

    @GET("search/tv")
    fun findTvs(@Query(Const.QUERY_API_KEY) apiKey: String, @Query(Const.QUERY_LANGUAGE) language: String, @Query(Const.QUERY) query : String): Observable<PageTvShow>
}