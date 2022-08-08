package com.jimmyworks.easyhttp.exception

/**
 * 類別無對應錯誤
 *
 * @author Jimmy Kang
 */
class HttpException : Exception {

    val url: String

    val httpCode: Int

    val responseBody: String?

    constructor(url: String, errorMessage: String) : super("url: $url, $errorMessage") {
        this.url = url
        this.httpCode = 0
        this.responseBody = null
    }

    constructor(url: String, e: Exception) : super("url: $url, ${e.message}") {
        this.url = url
        this.httpCode = 0
        this.responseBody = null
    }

    constructor(
        url: String,
        httpCode: Int,
        responseBody: String
    ) : super("url: $url, http code: $httpCode, responseBody: $responseBody") {
        this.url = url
        this.httpCode = httpCode
        this.responseBody = responseBody
    }
}
