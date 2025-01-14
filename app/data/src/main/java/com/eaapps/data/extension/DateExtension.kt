package com.eaapps.data.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


fun String.toDayOfWeek(inputPattern: String, textStyle: TextStyle = TextStyle.SHORT, locale: Locale = Locale.ENGLISH): String {
    val formatter = DateTimeFormatter.ofPattern(inputPattern)
    val date = LocalDate.parse(this, formatter)
    return date.dayOfWeek.getDisplayName(textStyle, locale)
}

fun String.formatDateTime(inputPattern: String, outputPattern: String, locale: Locale = Locale.ENGLISH): String {
    val inputFormatter = DateTimeFormatter.ofPattern(inputPattern)
    val outputFormatter = DateTimeFormatter.ofPattern(outputPattern, locale)
    val dateTime = LocalDateTime.parse(this, inputFormatter)
    return dateTime.format(outputFormatter)
}