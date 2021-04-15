package com.data.remote.entity.respond

/**
 * Respond data from API
 */
data class SecureRespond(
    val code: Int,
    val data: String,
    val message: String,
    val signature: String
) {
    fun isSuccess(): Boolean = code == 50200
}