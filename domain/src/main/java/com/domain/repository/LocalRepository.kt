package com.domain.repository

import com.data.local.LocalDataSource
import com.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(private val localDataSource: LocalDataSource) {
    suspend fun insertNote(title: String, detail: String) {
        localDataSource.insertNote(title, detail)
    }

    suspend fun fetchAllNotes(): Flow<List<NoteEntity>> {
        return localDataSource.fetchNotes()
    }
}