package com.example.movieapplication.data.api

import com.example.movieapplication.data.model.PageMovie
import com.example.movieapplication.utils.Const
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("discover/movie")
    fun getMovies(@Query(Const.QUERY_API_KEY) apiKey: String, @Query(Const.QUERY_LANGUAGE) language: String): Observable<PageMovie>
}