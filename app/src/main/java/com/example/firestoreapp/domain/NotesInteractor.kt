package com.example.firestoreapp.domain

import com.example.firestoreapp.domain.entity.Note
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NotesInteractor(private val notesRepo: NotesRepo) {

    fun getAllNotes() = notesRepo
        .getAllNotes()
        .map {
            val notes: MutableList<Note> = mutableListOf()
            notes.addAll(it)
            val uniqueDates: MutableSet<LocalDate> = mutableSetOf()

            notes.forEach { note ->
                uniqueDates.add(note.dateTime.toLocalDate())
            }

            uniqueDates.forEach { date ->
                notes.add(
                    Note(
                        dateTime = LocalDateTime.of(date, LocalTime.of(12, 0, 0)),
                        isHeader = true
                    )
                )
            }
            notes
        }.map { notes ->
            notes.sortWith(
                compareBy(
                    { it.dateTime.toLocalDate() },
                    { !it.isHeader },
                    { it.dateTime })
            )
            notes
        }

    fun saveNote(note: Note) = notesRepo.saveNote(note)

    fun deleteNote(note: Note) = notesRepo.deleteNote(note)

}