package com.example.firebase

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.NotesitemBinding

class NoteAdapter(private val notes: MutableList<CreateNote>,private val itemclicklistner:OnItemClickListener)
    : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
        interface OnItemClickListener{
            fun onDeleteClick(noteid:String)
            fun onUpdateClick(noteid:String,title:String,description:String)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.binding.btnUpdate.setOnClickListener {
            itemclicklistner.onUpdateClick(note.noteId,note.title,note.description)
        }
        holder.binding.btnDelete.setOnClickListener {
            itemclicklistner.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(val binding: NotesitemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: CreateNote) {
            binding.title.text = note.title
            binding.description.text = note.description
        }
    }
}
