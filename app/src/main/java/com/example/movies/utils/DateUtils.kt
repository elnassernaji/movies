package com.example.movies.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun format(d: String): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "MMMM dd, yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(d)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }
}