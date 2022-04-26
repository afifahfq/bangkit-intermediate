package com.example.storyapp.Helper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class LocalizationHelper {
    fun String.withDateFormat(date: String): String {
        val format = SimpleDateFormat("dd-MM-yyyy'T'HH:mm", Locale.US)
//        val date = format.parse(this) as Date

//        val stringDate = date.format(DateTimeFormatter.ISO_DATE_TIME)

        return DateFormat.getDateInstance(DateFormat.FULL).format(date)
    }
}