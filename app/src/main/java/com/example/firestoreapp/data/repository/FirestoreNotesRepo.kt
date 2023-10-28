package com.example.firestoreapp.data.repository

import android.util.Log
import com.example.firestoreapp.NOTES_COLLECTION
import com.example.firestoreapp.LOG_TAG
import com.example.firestoreapp.NO_ID
import com.example.firestoreapp.data.repository.mappers.toFirestoreEntity
import com.example.firestoreapp.data.repository.mappers.toNote
import com.example.firestoreapp.domain.NotesRepo
import com.example.firestoreapp.domain.entity.Note
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FirestoreNotesRepo : NotesRepo {
    private val sharedFlow = MutableSharedFlow<List<Note>>(1)
    private val notes = mutableListOf<Note>()

    private val db = Firebase.firestore.collection(NOTES_COLLECTION)

    init {
        db.get()
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
        observeDbChanges()
    }

    private fun observeDbChanges() {
        db.addSnapshotListener { value, _ ->
            value?.let {
                it.documentChanges.forEach { documentChange ->
                    val docData = documentChange.document.data
                    val docId = documentChange.document.id

                    when (documentChange.type) {

                        DocumentChange.Type.ADDED -> {
                            notes.add(docData.toNote(docId))
                        }

                        DocumentChange.Type.REMOVED -> {
                            val dataToRemove = notes.findLast { note ->
                                note.id == docId
                            }
                            notes.remove(dataToRemove)
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val dataToRemove = notes.findLast { note ->
                                note.id == docId
                            }
                            notes.remove(dataToRemove)
                            notes.add(docData.toNote(docId))
                        }

                        else -> {
                        }
                    }
                }
            }
            sharedFlow.tryEmit(notes)
        }
    }

    override fun getAllNotes(): SharedFlow<List<Note>> = sharedFlow

    override fun saveNote(note: Note) {
        if (note.id == NO_ID) {
            db.add(note.toFirestoreEntity())
        } else {
            db.document(note.id)
                .set(note.toFirestoreEntity())
        }
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
    }

    override fun deleteNote(note: Note) {
        db.document(note.id).delete()
    }
}