package com.example.stage4phrases

import android.app.Application
import androidx.room.Room
import com.example.stage4phrases.data.AppDatabase

class PhrasesApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        ).allowMainThreadQueries().build()
    }
}