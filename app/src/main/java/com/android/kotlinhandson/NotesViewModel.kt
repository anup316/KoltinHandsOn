package com.android.kotlinhandson

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.kotlinhandson.dao.Note
import com.android.kotlinhandson.dao.NotesDatabase
import com.android.kotlinhandson.dao.NotesRepository
import com.android.kotlinhandson.network.APIClient
import kotlinx.coroutines.launch

 class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository
    internal var _notesLiveData: LiveData<List<Note>>?


    init {
        val noteDao = NotesDatabase.getDatabase(application.applicationContext,viewModelScope)?.notesDao()
        repository = NotesRepository(noteDao)
        _notesLiveData = repository.allNotes


       viewModelScope.launch {
          val data= APIClient.getService().getAllNotes()
           Log.v("Data size",data.size.toString())
       }

    }

    fun insertNoteToDataBase(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

     fun updateNoteToDataBase(note : Note) = viewModelScope.launch {
         repository.update(note)
     }

     fun deleteNoteFromDataBase(positon: Int) = viewModelScope.launch {
         val note: Note? = _notesLiveData?.value?.get(positon)
         note?.let {
             repository.delete(it)
         }

     }
}