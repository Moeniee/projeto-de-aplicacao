package com.example.noteapp.ui

import android.R.attr.text
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.data.NoteDao
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.repository.NoteRepository
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.viewmodel.NoteViewModel
import com.example.noteapp.viewmodel.NoteViewModelFactory
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel

    private var adapter = NoteAdapter{note ->
        val intent = Intent(this, NoteEditor::class.java)
        intent.putExtra("NOTE", note)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        observeNotes()
        setupSearch()

        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(this, NoteEditor::class.java))
        }
    }
    private fun setupViewModel() {
        val dao = NoteDatabase.getDatabase(this).noteDao()
        val repo = NoteRepository(dao)
        val factory = NoteViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter
    }

    private fun observeNotes() {
        viewModel.allNotes.observe(this) {notes ->
            adapter.setNotes(notes)
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener{text ->
            viewModel.searchNotes(text.toString()).observe(this) {
                adapter.setNotes(it)
            }
        }
    }



}