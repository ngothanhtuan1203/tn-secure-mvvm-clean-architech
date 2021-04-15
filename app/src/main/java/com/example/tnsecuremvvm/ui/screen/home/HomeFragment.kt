package com.example.tnsecuremvvm.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.domain.model.Article
import com.example.tnsecuremvvm.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.layout_error.*

@AndroidEntryPoint
class HomeFragment :  Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: NewsApdater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setupViewModel()
        setupUI(root)
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadHotNewsData(
            ""
        )
    }

    //ui
    private fun setupUI(root: View) {
        adapter = NewsApdater(
            viewModel.article.value ?: emptyList()
        )
        root.recyclerView.layoutManager = LinearLayoutManager(this.context)
        root.recyclerView.adapter = adapter
        root.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this@HomeFragment.context,
                LinearLayoutManager.VERTICAL
            )
        )
        root.swipe_layout.setOnRefreshListener {
            swipe_layout.isRefreshing = false
            viewModel.loadHotNewsData(
                ""
            )

        }


    }

    //view model
    private fun setupViewModel() {
        viewModel.article.observe(viewLifecycleOwner, renderArticles)
        viewModel.isViewLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.isEmptyList.observe(viewLifecycleOwner, emptyListObserver)
    }

    //observers

    private val renderArticles = Observer<List<Article>> {
        //TNLog.d(TAG, "data updated $it")
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.update(it)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        //TNLog.v(TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        //TNLog.v(TAG, "onMessageError $it")
        layoutError.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        textViewError.text = "Error $it"
        recyclerView.visibility = View.GONE
    }

    private val emptyListObserver = Observer<Boolean> {
        //TNLog.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
        recyclerView.visibility = View.GONE

    }

    companion object {
         val TAG = HomeFragment::class.simpleName
    }


}
