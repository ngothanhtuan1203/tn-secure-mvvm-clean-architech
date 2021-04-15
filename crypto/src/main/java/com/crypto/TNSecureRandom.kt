package com.crypto

import android.util.Base64
import java.security.SecureRandom

internal class TNSecureRandom {
    fun randomString(length: Int): String {
        val ab = "0123456789abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM"
        val num = "0123456789abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM"
        val rnd = SecureRandom()
        val sb = StringBuilder(length)
        sb.append(num[rnd.nextInt(num.length)])
        for (i in 0 until length - 1) sb.append(ab[rnd.nextInt(ab.length)])
        return sb.toString()
    }

    fun nextKey(length: Int): String {
        val bytes: ByteArray? = randomBytes(length)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun randomBytes(length: Int): ByteArray {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return bytes
    }
}