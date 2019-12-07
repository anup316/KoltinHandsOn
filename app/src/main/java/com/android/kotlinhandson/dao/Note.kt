package com.android.kotlinhandson.dao

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "NotesList")
data class Note(

    @PrimaryKey
    @ColumnInfo(name = "note_title")
    val notesTitle: String,
    @ColumnInfo(name = "note_desc")
    val notesDescription: String,
    @ColumnInfo(name = "note_creation_date")
    val notesCreationDate: Date
)