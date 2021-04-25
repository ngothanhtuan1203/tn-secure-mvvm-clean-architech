package com.data.local

import com.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertNote(title: String, detail: String)
    suspend fun fetchNotes(): Flow<List<NoteEntity>>
}