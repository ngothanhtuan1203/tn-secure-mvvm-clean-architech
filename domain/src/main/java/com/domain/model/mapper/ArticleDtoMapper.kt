package com.domain.model.mapper

import com.data.remote.entity.respond.news.ArticleDto
import com.domain.model.Article

class ArticleDtoMapper : DomainMapper<ArticleDto, Article> {

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