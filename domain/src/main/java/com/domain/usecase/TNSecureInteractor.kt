package com.domain.usecase

import com.domain.model.Note
import com.domain.model.mapper.ArticleDtoMapper
import com.domain.model.mapper.NoteDtoMapper
import com.domain.repository.LocalRepository
import com.domain.repository.RemoteRepository
import javax.inject.Inject

class TNSecureInteractor @Inject constructor(private val remoteRepository: RemoteRepository, private val localRepository: LocalRepository) : TNSecureUseCase {
    override suspend fun getAllHotNews(selectedTitle: String): TNSecureUseCase.Result {
        val articleDtoMapper: ArticleDtoMapper = ArticleDtoMapper()
        var hotnewsRespond = remoteRepository.getHotNews(selectedTitle)

        return if (hotnewsRespond.isSuccess) {
            val articleDtoList = hotnewsRespond.data?.articles
            val cleanArticalList = articleDtoList?.filter { it.author != null }

            val modelList = cleanArticalList?.let { articleDtoMapper.toDomainList(it) }
            TNSecureUseCase.Result.Success(modelList)
        } else {
            TNSecureUseCase.Result.Failure(hotnewsRespond.message)
        }
    }

    override suspend fun insertNote(title: String, detail: String) {

        localRepository.insertNote(title, detail)
    }

    override suspend fun fetchAllNotes(): List<Note> {
        val noteDtoMapper = NoteDtoMapper()
        val list = localRepository.fetchAllNotes();
        return noteDtoMapper.toDomainList(list)
    }
}