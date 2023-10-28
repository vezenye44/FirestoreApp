package com.example.firestoreapp.data.repository.mappers

import com.example.firestoreapp.domain.entity.Note
import com.example.firestoreapp.utils.toStdFormatString
import com.example.firestoreapp.utils.toStdLocalDate
import com.example.firestoreapp.utils.toStdLocalTime
import java.time.LocalDateTime

fun Note.toFirestoreEntity(): HashMap<String, Any> {
    val firestoreEntity = HashMap<String, Any>()
    firestoreEntity["date"] = this.dateTime.toLocalDate().toStdFormatString()
    firestoreEntity["time"] = this.dateTime.toLocalTime().toString()
    firestoreEntity["pressure1"] = this.pressure1
    firestoreEntity["pressure2"] = this.pressure2
    firestoreEntity["pulse"] = this.pulse

    return firestoreEntity
}

fun Map<String, Any>.toNote(docId: String): Note {
    val date = this["date"].toString().toStdLocalDate()
    val time = this["time"].toString().toStdLocalTime()
    val pressure1 = this["pressure1"].toString().toInt()
    val pressure2 = this["pressure2"].toString().toInt()
    val pulse = this["pulse"].toString().toInt()
    return Note(
        id = docId,
        dateTime = LocalDateTime.of(date, time),
        pressure1 = pressure1,
        pressure2 = pressure2,
        pulse = pulse
    )
}