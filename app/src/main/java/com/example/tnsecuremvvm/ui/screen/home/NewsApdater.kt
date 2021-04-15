package com.example.tnsecuremvvm.ui.screen.home

import android.view.View
import com.domain.model.Article
import com.example.tnsecuremvvm.R
import com.example.tnsecuremvvm.ui.base.BaseAdapter

class NewsApdater(private var articles: List<Article>) :
    BaseAdapter<Article, ArticleViewHolder>(articles, R.layout.row_news) {

    override fun initViewHolder(rootView: View): ArticleViewHolder {
        return ArticleViewHolder(rootView)
    }

}