package com

import com.crypto.di.TNCryptoInject
import org.apache.commons.codec.binary.Base64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CryptoUnitTest {

    private val crypto = TNCryptoInject.provideTNCrypto()
    private val secretKey = "X2,H-s6@L+tdyC.S"

    private val SERVER_PUB_CERT : String =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgEFF5yaQFOQNLq7npN7yAaXTKsUFUq8i0F0K1kcS4k/38k8SHhFhFhzLVlGelHoIRGyfWE9Xovgvz7XfeIwxsvg4wroQUGUA3jlfIxyDNHFKLgiQXnfyLFptF9O3Z2PhJkZ8exh6yq85g24tpSEfq7g0JipmUf/2vUxL56TfUiA7f8Z6cQdQ/WXFMCvtonQeMKKOATMfRoHirZx7x75Dn1K7EonBtMDdIKriu4RbkWBGhMB8QgNxGZ2IQQI7k6i1IvH4/t9qhS+0e9+0nPPtSTBwp1CWo2wCXUYF4v3pq9NtlsJmzL/NHsTEOg+zl3KEmUfHLVrf70QF5nHsIzdgbQIDAQAB"
    private val CLIENT_PUBLIC_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgSGFq3zt2wBupybh9hTphE/JAYkpCZT5bwHXyU08loyDz5opan//e7V/TcGgpRGDXva/KBeAqsJ5vhl64nBHeq1qzD6rp63i9ztJt0yPTGp5RWtXFrROQlgVt/q0670MPgteTAACaje33UgoMa91SQc27bRePaHHS3uX3AClT4U+QlyBaIA7f/9V9BtSZ9ivpU5C+7dybqPuYyAvtnJspEB8w1X/77rBRM61xD8E5F7/VfTebAqOCwyIkeml4kWUezm0D8m8I17Gwdh3UowMyCfPKRKkQjE6fYKkibeOeW7mCVgV0gbPvf5ufnj/NppSu5xbG9ou/Cj9J57fTkyUJQIDAQAB"
    val aesRandomKey = crypto.randomBytes(32)

    private val encryptedPrivateKey = "_r55KJWlfMD2V0m2ScTV5nlUcxGItiYID7ZlqMb3nX06XINb0z90esbXUOtaZRpXg-gYAWWxb7G-hX-LIV_Kw5vUVafS1OLgKYPQ9eIo83y2ml6Fx1-kOVkwjL0mOgKSn5VBZVTSt2RqNjl7gCHgX0kRjJNoevNBUCZ4KFMtVD1u3trsOEw5MPoidiye0dRe3E4eI1Xt4kkHYHN1-O8_uunwEqZtD6TiSOpKREFlvK3e-LSjFDYSC3GGxdUXE2GKQ9o6xnJW8eKpzXzWstrDg6PvcLFzOpX6o-9RjnLA104DWC9aUVpC4UEnKiJXaig_4mu97gR4W5wynR83-jUqWyQw-tVu5n0NFp7ySaRdIFddIDv6nmYAZTPTkHuFTooIz3fhlF8-3k7oUmhQOF2RNtZa23LWGh8v67E6pBENgHwOYGvDH0jStG71c-4sBzNYQ3iWe3hPu_ZT9yTH9CEaZwm9kycXna3Bv_VoCv8Kw5EBJnNFBdY8NdE2XDMT_x-4CoTOTpCxQhH0feg2CeGul0Op9bBE8d7EneD1pCCZ1kHZVqRMzZ42aANWJV8tL7TqPhUHw1lu_fYgyPoeYSc4mlghZmBQaSeGfkHQmvKa55hJnot7j3e4-eH9HQA_bvYDGTVGzEC7jOW9LQJWYL3ZRCuoeURev18bLcoZq0IL2Eok3qW-0vX0XvH5e8ZbR9Wrta7jm6oUVIQoVm5_ZVlaPxJTIhmpQQDAKcbiZNtKQa2_BSzv0745owVrwzUwFMkq18yXawFNxgFMQKaFvNwoLrGMSZGgs_GLZ6M8w2He7X48KbuEnDfSerqBZSb4C2G8kvzGcH_DJnDyPz7b-LyKoVtZKnPcnRDsjYi4Sj4xKrYdIUe2Q73MNnhJOGpOVZ_61mpGFudEJRUWtG5L8rbirZbC_PwrUWaIx9vF5uftiqlwGqwU4wHrTGMlhpyRYkGSCbplKuidxrMcwxB1W3dSZGH8WK5TMq-GfmNqkN9ys129DgqNRdtZWGRdRlrBbrr7d0SruzGAzz7al4v7okZDIGK8bCrlQLDt0CYvTeyZFjtOuHyg1Rptfw2DCrnEsQN6U5zqzGefKEbTqvuiiIc14xHLyNv_6Xy5deDRa7Dgx-yq8PAVgNFvvdfGrDBlGZaDjKIRUlLJM_9DL4JDAy-TPAgwww2CkAo-R68YsQCNwjP5BG9CpRVKSlEgRzFbKBI7mzzjRfnkx2xxphUV8-bvbuwnfBO027NvTHfwBqE1--YcYHNkwimZjWrU7OjLNQlO0O0stvQaJ6G2Hadw-ekbn8HtwpPqTniE0IXxaK88IbYxXtdHywy0MnQVROvSw4ZZdKpKI4tyBnPdWpLNFJ40eiDMF7AvUaQUl_x3Ia9qiqPBpKPgtIgmLPz0U5TcB2jbGm9eDDrSUjPnKs94NobkDq222ZRcat1n2AX3Nxk7XAkmSH9t4au_SV_DOoL8E7ckAUHOEU2AWDh1__cCX9j-f6Brxg_m7kisY1O8-fA3jVpXzVTUyvR37_cotYpOZErj_75lSrfylkXwQradc8I6sb0BPEKqKA5QYHNrRGxUvjs2LaLB1GCXJikkopf3tjzhoZLtiFEUGHQ-NJYwPkTbnVyloYvjB9ILslDGuSOEAWGE65au6O3cLbWrxj8s6lF-wBqVfPFb8FakkzpmdHJYKxnEzVDraWJgcWUoex2Xe7LP6FPm7G6Ti7uWJVKBWJQN-T5WBjCHusgjUwvml7VqVbiuWgPRn7cqm8tu22-YjuGqE3mUelt4SilbATMig9GNQhzDf24ZpFpsAvffjVLuWoyyIFdFRZknj_HpZJktdWe0dvcSY4xdzJmLiF92QFMKmfaGAqGjIbMaeojRWsXcdzWRNo1kQGl2MgcFUbW5FxW5-Kb5cn-RmEbf-9eooE0aMTBNbfuIKMCJAhnBVAHOPLO_U4g8lnNHvbueLu3VGwYMhZCJTGxLV_EZUmDimAQeqaEMwL9jD-MY1_lhS09YPRKBmf4_M2vHcfFRZDMsH1YXJ7TnNpxjaGclvkfj1NUHuWNij2_JQ2UHkx3Uw_ZwWuyQHnPsPB0YRU_Y6PYriLctKrz8kYp1MwZ9zV_QwFII-nO5W4t3Ebz0uN5rIBozzclNMV_tuMYN6V5cSf3KPbjmohEpZi-nzt5cfAQZ6wHZ8PacOu2NNyRw46FkPdTpn0tmNIazSRnggpECzc0gzAU"

    @Test
    fun testInitOnesecslotNotNull() {
        val onsecSlot = crypto.initOnceSecSlot()
        assertNotNull(onsecSlot)
    }

    @Test
    fun testGetPublicKeyFromPemNotNull() {
        val publicKey = crypto.getPublicKeyFromPem(SERVER_PUB_CERT)
        assertNotNull(publicKey)
    }

    @Test
    fun testGetPrivateKeyNotNull() {
        val encryptedBytes = Base64.decodeBase64(encryptedPrivateKey)
        val privateKey = crypto.getPrivateKey(secretKey, encryptedBytes)
        assertNotNull(privateKey)
    }

    @Test
    fun testInitSessionKeyNotNull() {
        val publicKey = crypto.getPublicKeyFromPem(SERVER_PUB_CERT)
        val sessionkey = crypto.initSessionKey(publicKey, aesRandomKey)

        assertNotNull(sessionkey)
    }

    @Test
    fun testEncrypNotNull() {
        val tobeEncryptedData = " This is the data to be encrypted,have  some with it"
        val encryptedData = crypto.aesEncrypt(tobeEncryptedData, aesRandomKey)
        assertNotNull(encryptedData)
    }

    @Test
    fun testEncrypDecryptFlow() {
        val tobeEncryptedData = " This is the data to be encrypted,have  some with it"
        val encryptedData = crypto.aesEncrypt(tobeEncryptedData, aesRandomKey)
        val decryptedData = crypto.aesDecrypt(encryptedData, aesRandomKey)
        assertNotNull(decryptedData)
        assertEquals(tobeEncryptedData, decryptedData)
    }

    @Test
    fun testSigningNotNull() {
        val partial1 = "partial1"
        val partial2 = "spartial2"
        val partial3 = "tpartial3"
        val partial4 = "fpartial4"

        val tobeSignData = partial1 + partial2 + partial3 + partial4

        val privateKey = crypto.getPrivateKey(secretKey, Base64.decodeBase64(encryptedPrivateKey))
        val signedData = crypto.rsaSign(privateKey, tobeSignData)

        assertNotNull(signedData)
    }

    @Test
    fun testSigningSuccessFlow() {
        val partial1 = "partial1"
        val partial2 = "spartial2"
        val partial3 = "tpartial3"
        val partial4 = "fpartial4"

        val tobeSignData = partial1 + partial2 + partial3 + partial4
        val tobeCompareData = partial1 + partial2 + partial3 + partial4

        val privateKey = crypto.getPrivateKey(secretKey, Base64.decodeBase64(encryptedPrivateKey))
        val signedData = crypto.rsaSign(privateKey, tobeSignData)

        val publicKey = crypto.getPublicKeyFromPem(CLIENT_PUBLIC_KEY)
        assertNotNull(publicKey)

        val isVerified = crypto.verifySignedData(
            tobeCompareData.toByteArray(),
            Base64.decodeBase64(signedData),
            publicKey!!)

        assert(isVerified)
    }

    @Test
    fun testSigningFailedFlow() {
        val partial1 = "partial1"
        val partial2 = "spartial2"
        val partial3 = "tpartial3"
        val partial4 = "fpartial4"

        val tobeSignData = partial1 + partial2 + partial3 + partial4
        val tobeCompareData = partial1 + partial4 + partial3 + partial3

        val privateKey = crypto.getPrivateKey(secretKey, Base64.decodeBase64(encryptedPrivateKey))
        val signedData = crypto.rsaSign(privateKey, tobeSignData)

        val publicKey = crypto.getPublicKeyFromPem(CLIENT_PUBLIC_KEY)
        assertNotNull(publicKey)

        val isVerified = crypto.verifySignedData(
            tobeCompareData.toByteArray(),
            Base64.decodeBase64(signedData),
            publicKey!!)

        assert(!isVerified)
    }


}