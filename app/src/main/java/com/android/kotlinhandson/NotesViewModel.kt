package com.android.kotlinhandson

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.kotlinhandson.dao.Note
import com.android.kotlinhandson.dao.NotesDatabase
import com.android.kotlinhandson.dao.NotesRepository
import com.android.kotlinhandson.network.APIClient
import com.android.kotlinhandson.network.NotesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository
    var _notesLiveData: MutableLiveData<List<Note>>? = MutableLiveData()
    private val compositeDisposable:CompositeDisposable = CompositeDisposable()
    internal lateinit var job:Job


    init {
        val noteDao =
            NotesDatabase.getDatabase(application.applicationContext, viewModelScope)?.notesDao()
        repository = NotesRepository(noteDao)
    }

    /** Method to fetch the notes from remote when online and from room database when offline
     * @param context
     *
     * */
    fun getAllNotes(context: Context) {
        if (Utilities.isNetworkAvailable(context)) {
            job = viewModelScope.launch {
                val result = APIClient.getService(NotesService::class.java).getAllNotes()
                if (result.isSuccessful) {
                    val mappedList = result.body()?.map { note ->
                        Note(
                            note.title,
                            note.description,
                            Date(note.dateModified.toLong())
                        )
                    }
                    _notesLiveData?.value = mappedList
                } else {
                    Log.v("Error", result.errorBody()?.string())
                }
            }
        } else {
            _notesLiveData?.value = repository.allNotes?.value
        }
    }

    fun insertNoteToDataBase(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun updateNoteToDataBase(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun deleteNoteFromDataBase(positon: Int) = viewModelScope.launch {
        val note: Note? = _notesLiveData?.value?.get(positon)
        note?.let {
            repository.delete(it)
        }

    }

    /** This method basically uses RxAndroid to do concurrent work. using named parameter to handle success/failure scenarios
    *
    * */
    fun getAllNotesByRxJava() {
        val disposable: Disposable = APIClient.getService(NotesService::class.java).getAllObservableNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                println(it.size)
            }, onError = {
                print(it.message)
            }, onComplete = {

            })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        compositeDisposable.dispose()
    }
}