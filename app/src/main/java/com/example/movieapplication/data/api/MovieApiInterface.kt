package com.example.movieapplication.data.api

import com.example.movieapplication.data.model.PageMovie
import com.example.movieapplication.utils.Const
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("discover/movie")
    fun getMovies(@Query(Const.QUERY_API_KEY) apiKey: String, @Query(Const.QUERY_LANGUAGE) language: String): Observable<PageMovie>

    @GET("search/movie")
    fun findMovies(
        @Query(Const.QUERY_API_KEY) apiKey: String, @Query(Const.QUERY_LANGUAGE) language: String, @Query(
            Const.QUERY
        ) query: String
    ): Observable<PageMovie>

    @GET("discover/movie")
    fun movieRelease(
        @Query(Const.QUERY_API_KEY) apiKey: String, @Query(Const.QUERY_LANGUAGE) language: String, @Query(
            Const.QUERY_RELEASE_DATE_GTE
        ) dateGTE: String, @Query(Const.QUERY_RELEASE_DATE_LTE) dateLTE: String
    ): Observable<PageMovie>
}