package com.jimmyworks.easyhttp.entity

import okhttp3.Headers
import okhttp3.Request
import okio.Buffer

class RequestInfo {
    var url: String
    var contentType: String
    var headers: Headers
    var requestString: String?
    var request: Request

    constructor(request: Request) {
        this.url = request.url.toString()
        this.contentType = request.body?.contentType().toString()
        this.headers = request.headers
        val buffer = Buffer()
        request.body?.writeTo(buffer)
        this.requestString = buffer.readUtf8()
        this.request = request
    }

    constructor(request: Request, requestString: String) {
        this.url = request.url.toString()
        this.contentType = request.body?.contentType().toString()
        this.headers = request.headers
        this.requestString = requestString
        this.request = request
    }
}
