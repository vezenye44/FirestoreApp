package com.example.firestoreapp.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun LocalDate.toStdFormatString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun String.toStdLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun String.toStdLocalTime(): LocalTime {
    val time = this.split(":")
    return LocalTime.of(time[0].toInt(), time[1].toInt())
}

fun LocalDateTime.toHourMinString(): String {
    return "${this.hour.toString().padStart(2, '0')}:${this.minute.toString().padStart(2, '0')}"
}

