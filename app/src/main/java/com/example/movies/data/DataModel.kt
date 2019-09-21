package com.example.movies.data

import com.example.movies.model.*
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DataModel(private val api: Api<Request, Response>, private val gson: Gson) {

    fun fetchMovies(request: Request): Observable<MoviesResponse> {
        return api.get(request)
            .map {
                gson.fromJson(it.data, MoviesResponse::class.java)
            }.compose(applySchedulers())
    }

    fun fetchMovieDetails(request: Request): Observable<MovieDetailResponse> {
        return api.get(request)
            .map {
                gson.fromJson(it.data, MovieDetailResponse::class.java)
            }.compose(applySchedulers())
    }

    fun fetchSimilarMovies(request: Request): Observable<SimilarMoviesResponse> {
        return api.get(request)
            .map {
                gson.fromJson(it.data, SimilarMoviesResponse::class.java)
            }.compose(applySchedulers())
    }

    private fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}