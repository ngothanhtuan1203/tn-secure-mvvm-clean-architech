package com.data.local

import com.data.local.entity.NoteEntity
import com.data.local.room.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val noteDao: NoteDao) : LocalDataSource {

    override suspend fun insertNote(title: String, detail: String) {
        val noteEntity = NoteEntity(title, detail)
        return  noteDao.insertNote(noteEntity)
    }

    override suspend fun fetchNotes(): Flow<List<NoteEntity>> = flow {
        val datas = noteDao.getAllNotes()
        emit(datas)
    }

}