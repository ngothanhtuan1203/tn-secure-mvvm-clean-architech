package com.domain.model.mapper

import com.data.local.entity.NoteEntity
import com.domain.model.Note
import javax.inject.Inject

class NoteDtoMapper @Inject constructor(): DomainMapper<NoteEntity, Note> {
    override fun mapToDomainModel(model: NoteEntity): Note {
        return Note(id = model.id, title = model.canonicalTitle, detail = model.canonicalDetail)
    }
    fun toDomainList(initial: List<NoteEntity>): List<Note>{
        return initial.map { mapToDomainModel(it) }
    }
}