package com.note_entity

import com.DataFactory
import com.data.local.entity.NoteEntity


object NoteEntityFactory {

    fun generateDummyNoteEntities(size: Int): List<NoteEntity> {
        val mutableMovieEntityList = mutableListOf<NoteEntity>()
        repeat(size) {
            mutableMovieEntityList.add(generateNoteEntity())
        }

        return mutableMovieEntityList
    }


    fun generateNoteEntity(): NoteEntity {
        return NoteEntity(
            canonicalTitle = DataFactory.getRandomString(),
            canonicalDetail = DataFactory.getRandomString()
        )
    }
}
