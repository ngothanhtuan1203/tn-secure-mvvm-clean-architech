package com.data.remote.entity.request

/**
 * This class wrap request data to a secure package,
 * this included encryption and signing.
 */
data class SecureRequest(
        /**
         * Request data as JSon String
         */
        private var requestData: String,
        /**
         * Secure random session key with length 32bits
         */
        private var sessionKey: String,
        /**
         * Time interval for request
         */
        private var onesecslot: String,
        /**
         * A nonce use for valid request
         */
        var nonce: String,
        /**
         * Signature to singed all the requested data
         */
        var signature: String
)