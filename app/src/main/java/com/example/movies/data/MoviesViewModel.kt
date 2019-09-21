package com.example.movies.data

import com.example.movies.model.Request

class MoviesViewModel(private val dataModel: DataModel) {

    fun getMovies(request: Request) = dataModel.fetchMovies(request)

    fun getMovieDetails(request: Request) = dataModel.fetchMovieDetails(request)

    fun getSimilarMovies(request: Request) = dataModel.fetchSimilarMovies(request)
}