package com.data.remote.entity.request

import com.data.util.API_KEY

data class NewsAPIRequest (
    val selectedTitle: String,
    val apikey:String = API_KEY
): BaseRequest(apikey)
