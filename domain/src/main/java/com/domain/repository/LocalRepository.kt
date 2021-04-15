package com.domain.repository

import com.data.local.LocalDataSource
import com.data.local.entity.NoteEntity
import javax.inject.Inject

class LocalRepository @Inject constructor(private val localDataSource: LocalDataSource) {
    suspend fun insertNote(title: String, detail: String) {
        localDataSource.insertNote(title, detail)
    }

    suspend fun fetchAllNotes(): List<NoteEntity> {
        return localDataSource.fetchNotes()
    }
}