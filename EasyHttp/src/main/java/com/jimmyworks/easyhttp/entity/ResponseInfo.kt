package com.jimmyworks.easyhttp.entity

import okhttp3.Headers
import okhttp3.Response
import java.io.File

class ResponseInfo {

    val code: Int
    val contentType: String?
    val headers: Headers
    val body: Any

    constructor(response: Response, body: String) {
        code = response.code
        contentType = response.headers["Content-Type"]
        headers = response.headers
        this.body = body
    }

    constructor(response: Response, body: File) {
        code = response.code
        contentType = response.headers["Content-Type"]
        headers = response.headers
        this.body = body
    }
}
