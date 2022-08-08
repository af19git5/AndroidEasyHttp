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

    fun contentType(contentType: String): PostRequestBuilder {
        this.contentType = contentType
        return this
    }

    fun stringBody(string: String): PostRequestBuilder {
        this.requestBodyString = string
        return this
    }

    fun jsonBody(obj: Any): PostRequestBuilder {
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
