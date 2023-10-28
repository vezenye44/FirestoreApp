package com.example.firestoreapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firestoreapp.domain.NotesInteractor
import com.example.firestoreapp.domain.entity.Note
import com.example.firestoreapp.utils.createMutableSingleEventFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(private val interactor: NotesInteractor) : ViewModel() {
    companion object {
        const val FAKE_DELAY = 0L
    }

    private var job: Job? = null

    private val _resultRecycler = MutableStateFlow<List<Note>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _noteIdFlow = createMutableSingleEventFlow<Note>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()


    fun loadData() {
        job?.cancel()
        job = viewModelScope.launch {
            interactor.getAllNotes()
                .onStart { _isLoading.tryEmit(true) }
                .onEach {
                    _isLoading.tryEmit(true)
                    delay(FAKE_DELAY)
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }.collect()
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            interactor.saveNote(note)
        }
    }

    fun onEditNoteClicked(note: Note) {
        _noteIdFlow.tryEmit(note)
    }

    fun onDeleteNoteClicked(note: Note) {
        viewModelScope.launch {
            interactor.deleteNote(note)
        }
    }


}