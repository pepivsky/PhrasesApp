package com.example.stage4phrases.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stage4phrases.Constants

@Entity(tableName = Constants.DATABASE_TABLE)
data class Phrase(
    val phrase: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
