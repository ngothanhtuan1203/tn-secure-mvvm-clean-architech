package com.domain.usecase

import com.domain.model.Article
import com.domain.model.Note
import com.domain.model.mapper.ArticleDtoMapper
import com.domain.model.mapper.NoteDtoMapper
import com.domain.repository.LocalRepository
import com.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import javax.inject.Inject

class TNSecureInteractor @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val articleDtoMapper: ArticleDtoMapper
) : TNSecureUseCase {
    override suspend fun getAllHotNews(selectedTitle: String): TNSecureUseCase.Result {
        val hotnewsRespond = remoteRepository.getHotNews(selectedTitle)
        var result: TNSecureUseCase.Result = TNSecureUseCase.Result.Success(emptyList<Article>())

        hotnewsRespond.collect {

            result = if (it.count() > 0) {
                val modelList = articleDtoMapper.toDomainList(it)
                TNSecureUseCase.Result.Success(modelList)
            } else {
                TNSecureUseCase.Result.Failure("Some thing was wrong")
            }

        }
        return result
    }

    override suspend fun insertNote(title: String, detail: String) {

        localRepository.insertNote(title, detail)
    }

    override suspend fun fetchAllNotes(): List<Note> {
        val noteDtoMapper = NoteDtoMapper()
        val notes = localRepository.fetchAllNotes()
        var result = emptyList<Note>()
        notes.collect {
            result = noteDtoMapper.toDomainList(it)
        }
        return result
    }
}