package com.example.tnsecuremvvm.ui.screen.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.domain.model.Article
import com.example.tnsecuremvvm.R
import com.example.tnsecuremvvm.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.row_news.view.*

class ArticleViewHolder(view: View) : BaseViewHolder<Article>(view) {

    private val title: TextView = view.title_textview
    private val thumbImg: ImageView = view.thump_imageView

    private val author: TextView = view.author_textview
    private val time: TextView = view.times_textview
    private val topic: TextView = view.toppic_textview

    override fun bind(article: Article) {
        thumbImg.load(article.urlToImage) {
            crossfade(true)
            crossfade(1000)
            transformations(RoundedCornersTransformation(20f))
        }

        title.text = article.title
        author.text = article.author
        time.text = " - " + article.publishedAt
        topic.text = " - " + article.source

        itemView.setOnClickListener {
            val bundle = bundleOf("url" to article.url)
            itemView.findNavController()
                .navigate(R.id.action_navigation_home_to_DetailActivity, bundle)
        }
    }
}