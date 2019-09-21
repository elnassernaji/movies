package com.example.movies

import android.view.View

inline fun <reified T: View> androidx.fragment.app.Fragment.find(id: Int) = view?.findViewById<T>(id)

inline fun <reified T: View> View.find(id: Int): T = findViewById(id)