package com.example.selfconfidencefit.di

import android.app.Application
import androidx.room.Room
import com.example.selfconfidencefit.data.local.DatabaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application) : DatabaseApp {
        return Room.databaseBuilder(
            application,
            DatabaseApp::class.java,
            "selfconfidencemain.db"
        ).build()
    }
}