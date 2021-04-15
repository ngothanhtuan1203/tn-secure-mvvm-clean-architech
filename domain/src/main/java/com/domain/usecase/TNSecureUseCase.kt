package com.domain.usecase

import com.domain.model.Note

interface TNSecureUseCase {
    sealed class Result {
        data class Success<T>(val data: T) : Result()
        data class Failure(val message: String) : Result()
    }

    suspend fun getAllHotNews(selectedTitle: String): Result
    suspend fun insertNote(title: String, detail: String)
    suspend fun fetchAllNotes(): List<Note>
}