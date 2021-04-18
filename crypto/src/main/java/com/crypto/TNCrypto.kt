package com.crypto

import com.crypto.di.TNCryptoInject
import com.tnsecure.Secrets
import com.tnsecure.logs.TNLog
import org.apache.commons.codec.binary.Base64
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec

class TNCrypto() {
    /**
     * Client private key, encrypted by Secret key(AES algorithm) and store to bin.dat file.
     */
    private var tnRSAHelper: TNRSAHelper = TNCryptoInject.provideRSAHelper()
    private var aesHelper: TNAESHelper = TNCryptoInject.provideAESHelper()
    private var tnSecureRandom: TNSecureRandom = TNCryptoInject.provideTNSecureRandom()

    fun initOnceSecSlot(): String {
        return DateUtils.getDatetimeUTCFormat()
    }

    fun initSessionKey(publicKey: PublicKey?, bTek: ByteArray): String {
        return tnRSAHelper.rsaEncrypted(bTek, publicKey)
    }


    fun aesEncrypt(bodyDataPLain: String, encKey: ByteArray): String {
        return try {

            val aesFinalResult = aesHelper.encrypt(bodyDataPLain.toByteArray(), encKey)
            Base64.encodeBase64URLSafeString(
                aesFinalResult
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun aesDecrypt(base64EncryptedData: String, encKey: ByteArray): String {
        return try {
            val encryptedByteRaw = Base64.decodeBase64(
                base64EncryptedData
            )
            val decrypted = aesHelper.decrypt(encryptedByteRaw, encKey)
            String(aesHelper.unpad(decrypted))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Get public key from PEM certificate
     */
    fun getPublicKeyFromPem(inputKeyPem: String): PublicKey? {
        try {
            var publicKeyPEM =
                inputKeyPem.replace("-----BEGIN PUBLIC KEY-----\n", "")
            publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "")
            val decoded =
                Base64.decodeBase64(publicKeyPEM)
            return tnRSAHelper.getPublicKeyFromDecodedPem(decoded)
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    fun getPrivateKey(secretKey: String, encryptedFromFile: ByteArray): PrivateKey? {
        // read encrypted private key from device internal storage
//        val encryptedFromFile = readFileResource(javaClass, dataPath)
//        val encryptedFileInbase64 = Base64.encodeBase64URLSafeString(encryptedFromFile)
        // decrypted private key by secret key.
        val decryptedKey = aesHelper.decrypt(encryptedFromFile, secretKey.toByteArray())
        return getPrivateKeyFromPem(String(decryptedKey))
    }

    fun verifySignedData(
        data: ByteArray?,
        signedData: ByteArray?,
        pubKey: PublicKey
    ): Boolean {
        return tnRSAHelper.verifySignedData(data, signedData, pubKey)
    }

    fun rsaSign(
        privateKey: PrivateKey?,
        dataTobeSign: String
    ): String {
        return tnRSAHelper.rsaSign(privateKey, dataTobeSign)
    }

    fun nextKey(length: Int): String {
        return tnSecureRandom.nextKey(length)
    }

    fun getPrivateKeyFromPem(inputPemFile: String): PrivateKey? {
        val privateKey: PrivateKey? = null
        try {
            val decoded = Base64.decodeBase64(
                inputPemFile
            )
            val spec = PKCS8EncodedKeySpec(decoded)
            val kf = KeyFactory.getInstance(tnRSAHelper.PRKEY_RSA_ALGORITHM)
            return kf.generatePrivate(spec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            println("Could not reconstruct the private key, the given algorithm could not be found.")
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
            println("Could not reconstruct the private key")
        }
        return privateKey
    }

    fun getSecretKey(): String {
        val secretKey: String = Secrets().getEncKey("com.tnsecure")
        TNLog.d("CryptoUtil:", "secret key:" + secretKey)
        return secretKey
    }

    fun randomBytes(length: Int): ByteArray {
        return tnSecureRandom.randomBytes(length)
    }
}