package com.example.noteapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.databinding.ActivityNoteEditorBinding
import com.example.noteapp.repository.NoteRepository
import com.example.noteapp.viewmodel.NoteViewModel
import com.example.noteapp.viewmodel.NoteViewModelFactory

class NoteEditor : AppCompatActivity() {
    private lateinit var binding: ActivityNoteEditorBinding
    private lateinit var viewModel: NoteViewModel

    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()

        currentNote = intent.getParcelableExtra("NOTE")
        if(currentNote !=null){
            binding.etTitle.setText(currentNote!!.title)
            binding.etContent.setText(currentNote!!.content)
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupViewModel() {
        val dao = NoteDatabase.getDatabase(this).noteDao()
        val repo = NoteRepository(dao)
        val factory = NoteViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_editor, menu)
        menu.findItem(R.id.action_delete).isVisible = currentNote !=null
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_save -> {
                saveNote()
                true
            }
            R.id.action_delete -> {
                deleteNote()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun deleteNote() {
        if(currentNote !=null){
            viewModel.delete(currentNote!!)

        }
        finish()
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()

        if (title.isEmpty()){
            binding.etTitle.error = "Title is required"
            return
        }

        if (content.isEmpty()){
            binding.etContent.error = "Content is required"
            return
        }

        if (currentNote == null){
            val note = Note(title = title, content = content)
            viewModel.insert(note)
        }else{
            val note = currentNote!!.copy(title = title, content = content)
            viewModel.update(note)
        }

        finish()
    }
}