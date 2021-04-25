package com.domain.model.mapper

import com.data.remote.entity.respond.news.ArticleDto
import com.domain.model.Article
import javax.inject.Inject

class ArticleDtoMapper @Inject constructor() : DomainMapper<ArticleDto, Article> {

    override fun mapToDomainModel(model: ArticleDto): Article {
        return Article(author = model.author,
                title = model.title,
                url = model.url,
                urlToImage = model.urlToImage,
                publishedAt = model.publishedAt,
                source = model.source.name
        )
    }

    fun toDomainList(initial: List<ArticleDto>): List<Article>{
        return initial.map { mapToDomainModel(it) }
    }

}