package com.android.kotlinhandson

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlinhandson.dao.Note
import com.android.kotlinhandson.databinding.NotesRowBinding
import kotlinx.android.synthetic.main.notes_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class NotesAdapter(val list:ArrayList<Note>, val context: Context, val listener:CustomClickListener) : ItemTouchHelperAdapter,RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    override fun onItemMove(fromPosition: Int, toPosition: Int)  {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {

        val binding:NotesRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.notes_row, parent, false)
        return NotesViewHolder(binding)
       // return NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_row,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class NotesViewHolder(val binding: NotesRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(note: Note){
            binding.note = note
            binding.itemClickListener = listener
            binding.executePendingBindings()
        }
    }

    interface CustomClickListener {
        fun cardClicked(f: Note)
    }
}