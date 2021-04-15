package com.crypto

import android.util.Base64
import android.util.Base64.NO_WRAP
import com.tnsecure.logs.TNLog
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

internal class TNRSAHelper {
    private val TAG = TNRSAHelper::class.java.simpleName
    val PRKEY_RSA_ALGORITHM = "RSA"
    val RSA_ENCRYPTED_ALGORITHM = "RSA/ECB/OAEPPadding"
    val RSA_SIGN_ALGORITHM = "SHA256withRSA"

    fun getPublicKeyFromDecodedPem(decodedKey: ByteArray): PublicKey? {
        try {
            val spec =
                X509EncodedKeySpec(decodedKey)
            val kf =
                KeyFactory.getInstance(PRKEY_RSA_ALGORITHM)
            return kf.generatePublic(spec)
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Encrypted data with key follow 3 algorithm:
     * 1. RSA/ECB/OAEPPadding if key is RSA
     * 2. ECIES if key is ECC
     * 3. AES/CBC/NoPadding if key is AES
     *
     * @param datas     : data tobe encrypted
     * @param publicKey : key
     * @return
     */
    fun rsaEncrypted(datas: ByteArray?, publicKey: Key?): String {
        try {
            TNLog.d(
                TAG,
                "mtlssignature:encryptedDataWith creating Cipher with: $RSA_ENCRYPTED_ALGORITHM"
            )
            val cipher: Cipher = Cipher.getInstance(RSA_ENCRYPTED_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val result = cipher.doFinal(datas)
            return Base64.encodeToString(
                result,
                Base64.NO_WRAP or Base64.URL_SAFE
            )
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
        return ""
    }


    //Signature
    /**
     * Get signalture data of facial data.
     *
     * @param privateKey
     * @param dataTobeSign
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    @Throws(
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        SignatureException::class
    )
    fun rsaSign(
        privateKey: PrivateKey?,
        dataTobeSign: String
    ): String {
        val signer = Signature.getInstance(RSA_SIGN_ALGORITHM)
        signer.initSign(privateKey)
        signer.update(dataTobeSign.toByteArray())

        val signature = signer.sign()
        return Base64.encodeToString(signature, NO_WRAP)
    }

    /**
     *
     * @param data: data need to verify
     * @param signedData: signed data
     * @param pubKey: public key(key pair with private key using for sign signedData)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    @Throws(
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        SignatureException::class
    )
    fun verifySignedData(
        data: ByteArray?,
        signedData: ByteArray?,
        pubKey: PublicKey
    ): Boolean {
        val publickeyAlgorithm = pubKey.algorithm
        TNLog.d(
            TAG,
            "mtlssignature:verifySignature publickeyAlgorithm : $publickeyAlgorithm"
        )
        val signer: Signature = Signature.getInstance(RSA_SIGN_ALGORITHM)
        signer.initVerify(pubKey)
        signer.update(data)
        return signer.verify(signedData)
    }
}