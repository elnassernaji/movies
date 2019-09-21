package com.example.movies.data

import io.reactivex.Observable

interface Api<R, T> {
    fun get(request: R): Observable<T>
}