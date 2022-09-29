package com.jimmyworks.easyhttp.exception

/**
 * http錯誤
 *
 * @author Jimmy Kang
 */
class HttpException : Exception {

    override val message: String

    val url: String

    val httpCode: Int

    val responseBody: String?

    constructor(url: String, errorMessage: String) : super("url: $url, $errorMessage") {
        this.message = "url: $url, $errorMessage"
        this.url = url
        this.httpCode = 0
        this.responseBody = null
    }

    constructor(url: String, e: Exception) : super("url: $url, ${e.message}") {
        this.message = "url: $url, ${e.message}"
        this.url = url
        this.httpCode = 0
        this.responseBody = null
    }

    constructor(
        url: String,
        httpCode: Int,
        responseBody: String
    ) : super("url: $url, http code: $httpCode, responseBody: $responseBody") {
        this.message = "url: $url, http code: $httpCode, responseBody: $responseBody"
        this.url = url
        this.httpCode = httpCode
        this.responseBody = responseBody
    }
}
