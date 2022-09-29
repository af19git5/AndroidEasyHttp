package com.jimmyworks.easyhttp.builder

import android.content.Context
import com.jimmyworks.easyhttp.entity.RequestInfo
import com.jimmyworks.easyhttp.service.DoRequestService
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttp.utils.JsonUtils
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


/**
 * Request建構器(包含body層)
 *
 * @author Jimmy Kang
 */
class BodyRequestBuilder : RequestBuilder {

    private var contentType = "application/json"
    private var requestBodyString = ""
    private var requestBody: RequestBody? = null

    constructor(context: Context, url: String) : super(context, url, HttpMethod.POST)

    constructor(context: Context, url: String, httpMethod: HttpMethod) : super(
        context,
        url,
        httpMethod
    )

    override fun tag(tag: String): BodyRequestBuilder {
        super.tag(tag)
        return this
    }

    override fun addHeader(key: String, value: String): BodyRequestBuilder {
        super.addHeader(key, value)
        return this
    }

    override fun headers(headersMap: Map<String, String>): BodyRequestBuilder {
        super.headers(headersMap)
        return this
    }

    override fun addUrlParam(key: String, value: String): BodyRequestBuilder {
        super.addUrlParam(key, value)
        return this
    }

    override fun urlParams(urlParamsMap: Map<String, String>): BodyRequestBuilder {
        super.urlParams(urlParamsMap)
        return this
    }

    override fun cacheable(isCacheable: Boolean): BodyRequestBuilder {
        super.cacheable(isCacheable)
        return this
    }

    override fun saveRecord(isSaveRecord: Boolean): BodyRequestBuilder {
        super.saveRecord(isSaveRecord)
        return this
    }

    override fun connectTimeout(timeout: Long, timeUnit: TimeUnit): BodyRequestBuilder {
        super.connectTimeout(timeout, timeUnit)
        return this
    }

    override fun readTimeout(timeout: Long, timeUnit: TimeUnit): BodyRequestBuilder {
        super.readTimeout(timeout, timeUnit)
        return this
    }

    override fun writeTimeout(timeout: Long, timeUnit: TimeUnit): BodyRequestBuilder {
        super.writeTimeout(timeout, timeUnit)
        return this
    }

    fun stringBody(string: String): BodyRequestBuilder {
        contentType = "text/plain"
        this.requestBodyString = string
        return this
    }

    fun stringBody(contentType: String, string: String): BodyRequestBuilder {
        this.contentType = contentType
        this.requestBodyString = string
        return this
    }

    fun jsonBody(obj: Any): BodyRequestBuilder {
        contentType = "application/json"
        this.requestBodyString = JsonUtils.toJson(obj)
        return this
    }

    fun jsonBody(contentType: String, obj: Any): BodyRequestBuilder {
        this.contentType = contentType
        this.requestBodyString = JsonUtils.toJson(obj)
        return this
    }

    fun formBody(formMap: Map<String, String>): BodyRequestBuilder {
        val builder = FormBody.Builder(Charset.defaultCharset())
        for ((key, value) in formMap) {
            builder.add(key, value)
        }
        this.requestBody = builder.build()
        return this
    }

    fun formBody(formBody: FormBody): BodyRequestBuilder {
        this.requestBody = formBody
        return this
    }

    fun requestBody(requestBody: RequestBody): BodyRequestBuilder {
        this.requestBody = requestBody
        return this
    }

    override fun build(): DoRequestService {
        return DoRequestService(
            context,
            okHttpClientBuilder.build(),
            buildRequestInfo(),
            isSaveRecord
        )
    }

    private fun buildRequestInfo(): RequestInfo {

        requestBuilder.url(buildUrl())

        if (null != requestBody || !httpMethod.haveBody) {
            return RequestInfo(
                requestBuilder
                    .method(httpMethod.code, requestBody)
                    .build()
            )
        } else {
            return RequestInfo(
                requestBuilder
                    .method(
                        httpMethod.code,
                        requestBodyString.toRequestBody(contentType.toMediaType())
                    )
                    .build(),
                requestBodyString
            )
        }
    }
}
