package com.example.stage4phrases.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stage4phrases.data.model.Phrase
import kotlinx.coroutines.flow.Flow

@Dao
interface PhraseDao {

    @Insert
    fun insert(vararg phrase: Phrase)

    @Update
    fun updatePhrase(phrase: Phrase)

    @Delete
    fun delete(phrase: Phrase)

    @Query("SELECT * FROM phrases")
    fun getAll(): List<Phrase>

   /* @Query("SELECT * FROM phrase_table WHERE id = :phraseId")
    fun get(phraseId: Int): Flow<Phrase>*/

    @Query("SELECT * FROM phrases ORDER BY RANDOM() LIMIT 1")
    fun get(): Phrase

    @Query("SELECT EXISTS (SELECT * FROM phrases)")
    fun hasRecords(): Boolean
}