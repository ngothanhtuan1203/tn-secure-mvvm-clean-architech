package com.example.tnsecuremvvm.ui.screen.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Article
import com.domain.usecase.TNSecureUseCase
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val tnSecureUseCase: TNSecureUseCase
) : ViewModel(){
    private val _defaultAPIKey =
        MutableLiveData<String>().apply { value = "" }
    val defaultAPIKey: LiveData<String> = _defaultAPIKey

    private val _articles =
        MutableLiveData<List<Article>>().apply { value = emptyList() }
    val article: LiveData<List<Article>> = _articles

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList
    fun loadHotNewsData(selectedTitle: String) = viewModelScope.launch() {
        fetchHotNewsData(selectedTitle)
    }

    private suspend fun fetchHotNewsData(selectedTitle: String) {
        _isViewLoading.value = true

        when (val result:TNSecureUseCase.Result = tnSecureUseCase.getAllHotNews(selectedTitle)) {
            is TNSecureUseCase.Result.Success<*> -> {
                _isViewLoading.value = false
                val data = result.data as List<Article>
                if (data.isNullOrEmpty()) {
                    _isEmptyList.value = true

                } else {
                    _articles.value = data
                }
            }
            is TNSecureUseCase.Result.Failure -> {
                _isViewLoading.value = false
                _isEmptyList.value = true
                _onMessageError.value = result.message
            }
        }
    }


}
