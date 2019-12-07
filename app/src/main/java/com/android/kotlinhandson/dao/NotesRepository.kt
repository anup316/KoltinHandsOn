package com.android.kotlinhandson.dao

import androidx.lifecycle.LiveData

class NotesRepository(private val notesDao: NotesDao?) {

    val allNotes : LiveData<List<Note>>? = notesDao?.let { it.getAllNotes() }

    suspend fun insert(note: Note){
        notesDao?.insertNote(note)
    }

    suspend fun update(note :Note){
        notesDao?.updateNote(note)
    }

    suspend fun delete(note : Note){
        notesDao?.deleteNote(note)
    }

}