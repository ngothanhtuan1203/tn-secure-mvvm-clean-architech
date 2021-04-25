package com.data.local.room

import androidx.room.*
import com.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(notes: NoteEntity)

    @Update
    suspend fun updateNotes(notes: NoteEntity)
}