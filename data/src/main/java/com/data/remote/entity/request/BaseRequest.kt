package com.data.remote.entity.request

open class BaseRequest(apikey: String) {
    init {
        println("Initializing a BaseRequest class:" + apikey)
    }
}