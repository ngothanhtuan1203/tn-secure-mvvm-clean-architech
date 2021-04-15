package com.data.local

import com.data.local.entity.NoteEntity
import com.data.local.room.NoteDao
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val noteDao: NoteDao) : LocalDataSource {

    override suspend fun insertNote(title: String, detail: String) {
        val noteEntity = NoteEntity(title, detail)
        noteDao.insertNote(noteEntity)
    }

    override suspend fun fetchNotes(): List<NoteEntity> {
        return noteDao.getAllNotes()
    }

}