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
class PostRequestBuilder : RequestBuilder {

    private var contentType = "application/json"
    private var requestBodyString = ""
    private var requestBody: RequestBody? = null

    constructor(context: Context, url: String) : super(context, url, HttpMethod.POST)

    constructor(context: Context, url: String, httpMethod: HttpMethod) : super(
        context,
        url,
        httpMethod
    )

    override fun tag(tag: String): PostRequestBuilder {
        super.tag(tag)
        return this
    }

    override fun addHeader(key: String, value: String): PostRequestBuilder {
        super.addHeader(key, value)
        return this
    }

    override fun headers(headersMap: Map<String, String>): PostRequestBuilder {
        super.headers(headersMap)
        return this
    }

    override fun addUrlParams(key: String, value: String): PostRequestBuilder {
        super.addUrlParams(key, value)
        return this
    }

    override fun urlParams(urlParamsMap: Map<String, String>): PostRequestBuilder {
        super.urlParams(urlParamsMap)
        return this
    }

    override fun cacheable(isCacheable: Boolean): PostRequestBuilder {
        super.cacheable(isCacheable)
        return this
    }

    override fun saveRecord(isSaveRecord: Boolean): PostRequestBuilder {
        super.saveRecord(isSaveRecord)
        return this
    }

    override fun connectTimeout(timeout: Long, timeUnit: TimeUnit): PostRequestBuilder {
        super.connectTimeout(timeout, timeUnit)
        return this
    }

    override fun readTimeout(timeout: Long, timeUnit: TimeUnit): PostRequestBuilder {
        super.readTimeout(timeout, timeUnit)
        return this
    }

    override fun writeTimeout(timeout: Long, timeUnit: TimeUnit): PostRequestBuilder {
        super.writeTimeout(timeout, timeUnit)
        return this
    }

    fun stringBody(string: String): PostRequestBuilder {
        contentType = "text/plain"
        this.requestBodyString = string
        return this
    }

    fun stringBody(contentType: String, string: String): PostRequestBuilder {
        this.contentType = contentType
        this.requestBodyString = string
        return this
    }

    fun jsonBody(obj: Any): PostRequestBuilder {
        contentType = "application/json"
        this.requestBodyString = JsonUtils.toJson(obj)
        return this
    }

    fun jsonBody(contentType: String, obj: Any): PostRequestBuilder {
        this.contentType = contentType
        this.requestBodyString = JsonUtils.toJson(obj)
        return this
    }

    fun formBody(formMap: Map<String, String>): PostRequestBuilder {
        val builder = FormBody.Builder(Charset.defaultCharset())
        for ((key, value) in formMap) {
            builder.add(key, value)
        }
        this.requestBody = builder.build()
        return this
    }

    fun formBody(formBody: FormBody): PostRequestBuilder {
        this.requestBody = formBody
        return this
    }

    fun requestBody(requestBody: RequestBody): PostRequestBuilder {
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

        if (null != requestBody) {
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
