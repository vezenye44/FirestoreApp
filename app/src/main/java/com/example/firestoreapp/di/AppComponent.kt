package com.example.firestoreapp.di

import android.app.Application
import com.example.firestoreapp.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, MainModule::class, FragmentModule::class, ActivityModule::class, AndroidSupportInjectionModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun appModule(appProviderModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}