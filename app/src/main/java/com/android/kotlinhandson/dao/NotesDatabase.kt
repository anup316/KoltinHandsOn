package com.android.kotlinhandson.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


@Database(entities = [Note::class], version = 2)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao


    private class NoteDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.notesDao()

                    // Add sample words.
                    var word =
                        Note("New note", "This is new note", Date(System.currentTimeMillis()))
                    wordDao.insertNote(word)
                    word =
                        Note("New Note 1", "This is second note", Date(System.currentTimeMillis()))
                    wordDao.insertNote(word)

                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null
        val migration: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
               database.execSQL("ALTER TABLE NotesList " + "ADD COLUMN note_creation_date INTEGER DEFAULT 218989 NOT NULL")
            }
        }
        fun getDatabase(context: Context, scope: CoroutineScope): NotesDatabase? {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return INSTANCE
            }
            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_app_database"
                ).addCallback(NoteDatabaseCallback(scope))
                    .addMigrations(migration)
                    .build()
                INSTANCE = instance
                return INSTANCE
            }
        }
    }

}