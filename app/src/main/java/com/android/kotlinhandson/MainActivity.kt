package com.android.kotlinhandson

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.kotlinhandson.dao.Note
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NotesAdapter.CustomClickListener {

    lateinit var adapter: NotesAdapter

    override fun cardClicked(note: Note) {
        startActivity(Intent(baseContext, AddNoteActivity::class.java).let { it ->
            val bundle = Bundle()

            bundle.let {
                it.putString("title", note.notesTitle)
                it.putString("desc", note.notesDescription)
            }
            it.putExtras(bundle)
            it
        })
    }

    lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        recyclerView.layoutManager = LinearLayoutManager(this)


        add_note.setOnClickListener {
            startActivity(Intent(baseContext, AddNoteActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        viewModel._notesLiveData?.observe(
            this,
            Observer { data ->
                adapter = NotesAdapter(data as ArrayList<Note>, this, this)
                recyclerView.adapter = adapter
                val callback = NotesRecyclerViewItemTouchListener(adapter as ItemTouchHelperAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerView)
            })
    }

}
