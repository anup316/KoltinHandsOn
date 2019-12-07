package com.android.kotlinhandson.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface NotesService {

    @GET("notes/v1/getAllNotes")
    suspend fun getAllNotes(): List<Note>
}