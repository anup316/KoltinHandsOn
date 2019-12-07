package com.android.kotlinhandson

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.android.kotlinhandson.dao.Note

import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.content_add_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    lateinit var viewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = loadBitmap()
            url_image.setImageBitmap(bitmap)
        }


        add_note_btn.setOnClickListener {
            val notesTitle = note_header_edit.text.toString()
            val notesDesc = note_description_edit.text.toString()

            add_note_btn.let {
                if (it.text == "Edit Note") {
                    viewModel.updateNoteToDataBase(Note(notesTitle, notesDesc, Date(System.currentTimeMillis())))
                } else {
                    viewModel.insertNoteToDataBase(Note(notesTitle, notesDesc, Date(System.currentTimeMillis())))
                }
            }
            finish()
        }
    }

    private fun init() {
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(toolbar)
        intent?.let {
            it.extras?.let { ext ->
                note_header_edit.setText(ext.getString("title"))
                note_description_edit.setText(ext.getString("desc"))
                add_note_btn.text = "Edit Note"
            }
        }
    }

    private suspend fun loadBitmap(): Bitmap =
        withContext(Dispatchers.IO) {
            URL("https://www.gstatic.com/webp/gallery/1.jpg").openStream().use {
                return@withContext BitmapFactory.decodeStream(it)
            }
        }
}
