package com.example.tnsecuremvvm.ui.screen.myfeeds

import android.view.View
import android.widget.TextView
import com.domain.model.Note
import com.example.tnsecuremvvm.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.row_notes.view.*

class NoteViewHolder(view: View) : BaseViewHolder<Note>(view) {

    private val title: TextView = view.title_textview

    private val detail: TextView = view.detail_textview
    private val id: TextView = view.id_textview

    override fun bind(note: Note) {
        title.text = note.title
        detail.text = note.detail
        id.text = note.id.toString()
    }
}