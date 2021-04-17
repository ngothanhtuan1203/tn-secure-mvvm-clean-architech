package com.crypto

import com.crypto.di.TNCryptoInject
import com.tnsecure.Secrets
import com.tnsecure.logs.TNLog
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec

class TNCrypto() {
    /**
     * Client private key, encrypted by Secret key(AES algorithm) and store to bin.dat file.
     */
    val DATA = "res/raw/bin.dat"
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

    fun getPrivateKey(): PrivateKey? {
        val secretKey = Secrets().getEncKey("com.tnsecure")
        TNLog.d("CryptoUtil:", "secret key:" + secretKey);
        // read encrypted private key from device internal storage
        val encryptedFromFile = readFileResource(javaClass, DATA)
        // decrypted private key by secret key.
        val decryptedKey = aesHelper.decrypt(encryptedFromFile, secretKey.toByteArray())
        return getPrivateKeyFromPem(String(decryptedKey))
    }

    @Throws(IOException::class)
    private fun readFileResource(
        resourceClass: Class<*>,
        resourcePath: String
    ): ByteArray {
        val `is` =
            resourceClass.classLoader?.getResourceAsStream(resourcePath)
                ?: throw IOException("cannot find resource: $resourcePath")
        return getBytesFromInputStream(`is`)
    }

    @Throws(IOException::class)
    private fun getBytesFromInputStream(`is`: InputStream): ByteArray {
        val os = ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        var len: Int
        while (`is`.read(buffer).also { len = it } != -1) {
            os.write(buffer, 0, len)
        }
        os.flush()
        return os.toByteArray()
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
        return Secrets().getEncKey("com.tnsecure");
    }

    fun randomBytes(length: Int): ByteArray {
        return tnSecureRandom.randomBytes(length)
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
}