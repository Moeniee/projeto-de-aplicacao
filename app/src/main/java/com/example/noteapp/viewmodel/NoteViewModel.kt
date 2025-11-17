package com.example.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.Note
import com.example.noteapp.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val respository: NoteRepository) : ViewModel() {

    val allNotes: LiveData<List<Note>> = respository.allNotes

    fun insert(note: Note) = viewModelScope.launch {
        respository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        respository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        respository.delete(note)
    }

    fun searchNotes(query: String): LiveData<List<Note>> {
        return if(query.isEmpty()){
            allNotes
        }else{
            respository.searchNotes(query)
        }
    }

}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}