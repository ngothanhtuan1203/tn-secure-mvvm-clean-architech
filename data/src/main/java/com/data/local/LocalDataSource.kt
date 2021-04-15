package com.data.local

import com.data.local.entity.NoteEntity

interface LocalDataSource {
    suspend fun insertNote(title: String, detail: String)
    suspend fun fetchNotes(): List<NoteEntity>
}