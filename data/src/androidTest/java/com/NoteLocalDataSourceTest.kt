package com

import android.os.Build
import com.data.local.LocalDataSource
import com.data.local.LocalDataSourceImpl
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class NoteLocalDataSourceTest: DatabaseTest() {

    private var noteDao = appDatabase.noteDao()

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        localDataSource = LocalDataSourceImpl(noteDao)
    }

    @Test
    fun insertNoteToDBSuccess() = runBlockingTest {
       // localDataSource.insertNote("testInsert", "detailInsert")
        val a = 5;
    }

}
