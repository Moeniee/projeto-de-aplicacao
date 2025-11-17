package com.example.noteapp.ui.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NoteAdapter (private val onClick: (Note) -> Unit) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val noteList = ArrayList<Note>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        val note = noteList[position]

        holder.binding.apply {
            tvTitle.text = note.title
            tvContent.text = note.content
            tvDate.text = "Updated: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(note.updatedAt)}"

            root.setOnClickListener {
                onClick(note)
            }
        }
    }

    override fun getItemCount() = noteList.size

    inner class NoteViewHolder(val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root)

    fun setNotes(notes: List<Note>) {
        noteList.clear()
        noteList.addAll(notes)
        notifyDataSetChanged()
    }

}