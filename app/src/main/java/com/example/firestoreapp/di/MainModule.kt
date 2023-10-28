package com.example.firestoreapp.di

import com.example.firestoreapp.data.repository.FirestoreNotesRepo
import com.example.firestoreapp.domain.NotesInteractor
import com.example.firestoreapp.domain.NotesRepo
import com.example.firestoreapp.ui.MainActivity
import com.example.firestoreapp.ui.NoteDialogFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun notesRepo(): NotesRepo = FirestoreNotesRepo()


    @Provides
    @Singleton
    fun notesInteractor(repo: NotesRepo): NotesInteractor = NotesInteractor(repo)

}

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun bindNoteDialogFragment(): NoteDialogFragment
}