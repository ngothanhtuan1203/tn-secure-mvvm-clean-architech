package com.example.newsapi.ui.myfeeds

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Note
import com.domain.usecase.TNSecureUseCase
import kotlinx.coroutines.launch

class MyfeedsViewModel @ViewModelInject constructor(
    private val tnSecureUseCase: TNSecureUseCase
): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is note list save in DB"
    }
    val text: LiveData<String> = _text

    private val _notes =
        MutableLiveData<List<Note>>().apply { value = emptyList() }
    val notes: LiveData<List<Note>> = _notes

    fun addNote(title: String, detail: String) = viewModelScope.launch() {
        tnSecureUseCase.insertNote(title, detail)
        reloadNotes()
    }

    fun loadNotes() = viewModelScope.launch() {
        reloadNotes()
    }

   suspend fun reloadNotes() {
        var notes = tnSecureUseCase.fetchAllNotes()
        _notes.value = notes
    }
}