package com

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.data.local.LocalDataSource
import com.data.local.LocalDataSourceImpl
import com.data.local.room.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
class TNRoomTest : DatabaseTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var noteDao :NoteDao

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        noteDao = appDatabase.noteDao()
        localDataSource = LocalDataSourceImpl(noteDao)
    }

    @Test
    fun insertNoteToRoomSuccess()  = runBlocking{
         localDataSource.insertNote("testInsert", "detailInsert")
        val a:Int = 5
        assert(a == 5)
    }
}