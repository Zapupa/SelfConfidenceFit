package com.example.selfconfidencefit.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {
    @SuppressLint("SimpleDateFormat")
    private val standard = SimpleDateFormat("yyyy-MM-dd")
    fun standardFormat(date: Date): String { return standard.format(date) }
    fun standardParse(string: String): Date { return standard.parse(string)!! }

    @SuppressLint("SimpleDateFormat")
    private val readable = SimpleDateFormat("EEEE, d MMMM yyyy")
    fun readableFormat(date: Date): String { return readable.format(date) }
    fun readableParse(string: String): Date { return readable.parse(string)!! }

    @SuppressLint("SimpleDateFormat")
    private val standardDayOfTheWeek = SimpleDateFormat("EEEE", Locale("ru"))
    fun formatDayOfWeek(date: Date): String {return standardDayOfTheWeek.format(date)}
    fun parseDayOfWeek(string: String): String {
        val date = standardParse(string)
        return standardDayOfTheWeek.format(date)
    }
}