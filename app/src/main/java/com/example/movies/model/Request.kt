package com.example.movies.model

data class Request(val url: String, val params: Map<String, String>?= hashMapOf())