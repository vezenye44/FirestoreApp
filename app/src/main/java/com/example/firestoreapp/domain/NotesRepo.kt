package com.example.firestoreapp.domain

import com.example.firestoreapp.domain.entity.Note
import kotlinx.coroutines.flow.SharedFlow

interface NotesRepo {
    fun getAllNotes(): SharedFlow<List<Note>>
    fun saveNote(note: Note)
    fun deleteNote(note: Note)
}