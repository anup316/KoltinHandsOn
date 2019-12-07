package com.android.kotlinhandson.dao

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class NotesDao {

    @Query("SELECT * FROM NotesList")
    abstract fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertNote(note :Note)

    @Update
    abstract suspend fun updateNote(vararg note: Note)

    @Delete
    abstract suspend fun deleteNote(vararg note :Note)
}