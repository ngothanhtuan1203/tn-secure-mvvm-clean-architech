package com.example.tnsecuremvvm.ui.screen.myfeeds

import android.view.View
import com.domain.model.Note
import com.example.tnsecuremvvm.R
import com.example.tnsecuremvvm.ui.base.BaseAdapter

class NotesAdapter(private var notes: List<Note>) :
    BaseAdapter<Note, NoteViewHolder>(notes, R.layout.row_notes) {
    override fun initViewHolder(rootView: View): NoteViewHolder {
        return NoteViewHolder(view = rootView)
    }

}