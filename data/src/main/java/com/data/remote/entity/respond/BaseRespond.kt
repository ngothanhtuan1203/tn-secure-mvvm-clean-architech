package com.data.remote.entity.respond

data class BaseRespond<T>(
    val data: T?,
    val message: String,
    val isSuccess: Boolean
)