package com.crypto

import org.apache.commons.lang3.ArrayUtils
import java.security.InvalidKeyException
import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


internal class TNAESHelper {
    val TAG = TNAESHelper::class.java.simpleName
    private val IV_SIZE = 16
    private val AES_ENCRYPTED_ALGORITHM = "AES/CBC/PKCS5Padding"
    private val AES_KEY_TYPE = "AES"

    /**
     * Random secure bytes with length.
     *
     * @param length
     * @return
     */
    fun randomBytes(length: Int): ByteArray {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return bytes
    }

    private fun pad(message: ByteArray, blockSize: Int): ByteArray {
        val padSize = blockSize - message.size % blockSize
        val plaintextPadded = ByteArray(padSize)
        return ArrayUtils.addAll(message, *plaintextPadded)
    }

    /**
     * Generate Secret key from raw byte array data, the Key Type will follow algorithm
     *
     * @param data
     * @param algorithm
     * @return
     */
    fun getKeyFromByteArray(data: ByteArray?, algorithm: String?): Key {
        return SecretKeySpec(data, algorithm)
    }

    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        return try {
            val iv = randomBytes(IV_SIZE)
            val ivParameterSpec =
                IvParameterSpec(iv)

            // Encrypt.
            val aesCipher =
                Cipher.getInstance(AES_ENCRYPTED_ALGORITHM)
            val bTekKey =
                getKeyFromByteArray(key, AES_KEY_TYPE)
            aesCipher.init(Cipher.ENCRYPT_MODE, bTekKey, ivParameterSpec)
            val paddedText = pad(data, aesCipher.blockSize)
            val encrypted = aesCipher.doFinal(paddedText)
            return ArrayUtils.addAll(iv, *encrypted)
        } catch (e: Exception) {
            ByteArray(0)
        } catch (f: InvalidKeyException) {
            ByteArray(0)
        }
    }

    fun decrypt(data: ByteArray, key: ByteArray): ByteArray {
        return try {

            val iv: ByteArray =
                ArrayUtils.subarray(data, 0, IV_SIZE)
            val ivParameterSpec =
                IvParameterSpec(iv)

            // Extract encrypted part.
            val encryptedBytes: ByteArray? = ArrayUtils.subarray(
                data,
                IV_SIZE,
                data.size
            )
            // Decrypt.
            val cipherDecrypt =
                Cipher.getInstance(AES_ENCRYPTED_ALGORITHM)
            val bTekKey =
                getKeyFromByteArray(key, AES_KEY_TYPE)
            cipherDecrypt.init(Cipher.DECRYPT_MODE, bTekKey, ivParameterSpec)
            val decrypted = cipherDecrypt.doFinal(encryptedBytes)
            return unpad(decrypted)
        } catch (e: Exception) {
            return ByteArray(0)
        } catch (f: InvalidKeyException) {
            ByteArray(0)
        }
    }

    fun unpad(plaintextPadded: ByteArray): ByteArray {
        var size = 0
        for (b in plaintextPadded) {
            if (b == '\u0000'.toByte()) {
                break
            }
            size++
        }
        val finalBytes = ByteArray(size)
        var i = 0
        for (b in plaintextPadded) {
            if (b == '\u0000'.toByte()) {
                break
            }
            size++
            finalBytes[i] = b
            i++
        }
        return finalBytes
    }
}
