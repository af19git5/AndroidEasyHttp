package com.jimmyworks.easyhttp.builder

import android.content.Context
import com.jimmyworks.easyhttp.EasyHttpConfig
import com.jimmyworks.easyhttp.entity.RequestInfo
import com.jimmyworks.easyhttp.other.EasyHttpCookieJar
import com.jimmyworks.easyhttp.service.DoRequestService
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttp.utils.CommonUtils
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


/**
 * Request建構器
 *
 * @author Jimmy Kang
 */
open class RequestBuilder {

    protected val context: Context
    protected val url: String
    protected val httpMethod: HttpMethod
    protected val requestBuilder: Request.Builder = Request.Builder()
    protected val urlParamsMap: MutableMap<String, String> = HashMap()
    protected var isSaveRecord: Boolean = true

    val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    constructor(context: Context, url: String) {
        this.context = context
        this.url = url
        this.httpMethod = HttpMethod.GET
        initOkHttpClientBuilder()
    }

    constructor(context: Context, url: String, httpMethod: HttpMethod) {
        this.context = context
        this.url = url
        this.httpMethod = httpMethod
        initOkHttpClientBuilder()
    }

    private fun initOkHttpClientBuilder() {
        okHttpClientBuilder.cache(
            Cache(
                CommonUtils.getDiskCacheDir(
                    context, EasyHttpConfig.CACHE_DIR_NAME
                ),
                EasyHttpConfig.MAX_CACHE_SIZE
            )
        ).connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cookieJar(EasyHttpCookieJar(context))
    }

    open fun tag(tag: String): RequestBuilder {
        this.requestBuilder.tag(tag)
        return this
    }

    open fun addHeader(key: String, value: String): RequestBuilder {
        this.requestBuilder.header(key, value)
        return this
    }

    open fun headers(headersMap: Map<String, String>): RequestBuilder {
        for ((key, value) in headersMap) {
            this.requestBuilder.header(key, value)
        }
        return this
    }

    open fun addUrlParam(key: String, value: String): RequestBuilder {
        this.urlParamsMap[key] = value
        return this
    }

    open fun urlParams(urlParamsMap: Map<String, String>): RequestBuilder {
        for ((key, value) in urlParamsMap) {
            this.urlParamsMap[key] = value
        }
        return this
    }

    open fun cacheable(isCacheable: Boolean): RequestBuilder {
        if (isCacheable) {
            this.requestBuilder.cacheControl(CacheControl.Builder().build())
        } else {
            this.requestBuilder.cacheControl(CacheControl.Builder().noStore().build())
        }
        return this
    }

    open fun saveRecord(isSaveRecord: Boolean): RequestBuilder {
        this.isSaveRecord = isSaveRecord
        return this
    }

    open fun connectTimeout(timeout: Long, timeUnit: TimeUnit): RequestBuilder {
        okHttpClientBuilder.connectTimeout(timeout, timeUnit)
        return this
    }

    open fun readTimeout(timeout: Long, timeUnit: TimeUnit): RequestBuilder {
        okHttpClientBuilder.readTimeout(timeout, timeUnit)
        return this
    }

    open fun writeTimeout(timeout: Long, timeUnit: TimeUnit): RequestBuilder {
        okHttpClientBuilder.writeTimeout(timeout, timeUnit)
        return this
    }

    open fun build(): DoRequestService {
        val request = requestBuilder.url(buildUrl())
            .method(httpMethod.code, null).build()
        return DoRequestService(
            context,
            okHttpClientBuilder.build(),
            RequestInfo(request),
            isSaveRecord
        )
    }

    protected fun buildUrl(): String {
        val urlBuilder = StringBuilder(this.url)
        var i = 0
        for ((key, value) in this.urlParamsMap) {
            if (i == 0) {
                urlBuilder.append("?")
            }
            urlBuilder.append(key).append("=").append(value)

            if (i != urlParamsMap.size - 1) {
                urlBuilder.append("&")
            }
            i++
        }
        return urlBuilder.toString()
    }
}
