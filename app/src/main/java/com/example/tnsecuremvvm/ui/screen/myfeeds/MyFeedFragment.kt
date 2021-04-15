package com.example.tnsecuremvvm.ui.screen.myfeeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.domain.model.Note
import com.example.newsapi.ui.myfeeds.MyfeedsViewModel
import com.example.tnsecuremvvm.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.view.*

@AndroidEntryPoint
class MyFeedFragment : Fragment() {

    private val myfeedsViewModel: MyfeedsViewModel by viewModels()
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_feed, container, false)
        setupViewModel()
        setupUI(root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myfeedsViewModel.loadNotes()
    }

    private fun setupViewModel() {
        myfeedsViewModel.notes.observe(viewLifecycleOwner, renderNotes)
    }
    private fun setupUI(root: View) {

        val textView: TextView = root.findViewById(R.id.text_dashboard)
        myfeedsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        root.add_note_button.setOnClickListener {
            myfeedsViewModel.addNote(
                root.note_title_edit_text.text.toString(),
                root.note_detail_edit_text.text.toString()
            )
        }

        adapter = NotesAdapter(
            myfeedsViewModel.notes.value ?: emptyList()
        )
        root.note_list_recyclerView.layoutManager = LinearLayoutManager(this.context)
        root.note_list_recyclerView.adapter = adapter
        root.note_list_recyclerView.addItemDecoration(
            DividerItemDecoration(
                this@MyFeedFragment.context,
                LinearLayoutManager.VERTICAL
            )
        )


    }

    //observers
    private val renderNotes = Observer<List<Note>> {
        adapter.update(it)
    }
}