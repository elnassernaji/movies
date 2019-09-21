package com.example.movies

interface Binder<T> {
    fun bind(item: T)
}