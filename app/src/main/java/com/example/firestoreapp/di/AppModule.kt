package com.example.firestoreapp.di

import com.example.firestoreapp.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(app: App) {
    private val application: App

    init {
        this.application = app
    }

    @Provides
    @Singleton
    fun application(): App {
        return application
    }
}