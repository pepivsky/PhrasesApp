package com.example.stage4phrases.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stage4phrases.data.model.Phrase

@Database(entities = [Phrase::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getPhraseDao(): PhraseDao
}