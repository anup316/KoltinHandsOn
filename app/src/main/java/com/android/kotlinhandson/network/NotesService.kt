package com.android.kotlinhandson.network

import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.Observables
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface NotesService {

    @GET("notes/v1/getAllNotes")
    suspend fun getAllNotes(): Response<List<Note>>

    @GET("notes/v1/getAllNotes")
    fun  getAllObservableNotes() : Flowable<List<Note>>



}