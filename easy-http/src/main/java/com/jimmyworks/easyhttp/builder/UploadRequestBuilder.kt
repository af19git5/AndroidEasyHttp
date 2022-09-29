package com.jimmyworks.easyhttp.builder

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.jimmyworks.easyhttp.entity.RequestInfo
import com.jimmyworks.easyhttp.service.DoRequestService
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttp.utils.FileUtils.Companion.fileSizeText
import com.jimmyworks.easyhttp.utils.FileUtils.Companion.mimeType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 檔案上傳建構器
 *
 * @author Jimmy Kang
 */
open class UploadRequestBuilder : RequestBuilder {

    private val multipartBodyBuilder =
        MultipartBody.Builder().setType("multipart/form-data".toMediaType())
    private val requestStringBuilder = StringBuilder("--Form-data--\n")

    constructor(context: Context, url: String) : super(context, url, HttpMethod.POST)

    constructor(context: Context, url: String, httpMethod: HttpMethod) : super(
        context,
        url,
        httpMethod
    )

    override fun tag(tag: String): UploadRequestBuilder {
        super.tag(tag)
        return this
    }

    override fun addHeader(key: String, value: String): UploadRequestBuilder {
        super.addHeader(key, value)
        return this
    }

    override fun headers(headersMap: Map<String, String>): UploadRequestBuilder {
        super.headers(headersMap)
        return this
    }

    override fun addUrlParam(key: String, value: String): UploadRequestBuilder {
        super.addUrlParam(key, value)
        return this
    }

    override fun urlParams(urlParamsMap: Map<String, String>): UploadRequestBuilder {
        super.urlParams(urlParamsMap)
        return this
    }

    override fun cacheable(isCacheable: Boolean): UploadRequestBuilder {
        super.cacheable(isCacheable)
        return this
    }

    override fun saveRecord(isSaveRecord: Boolean): UploadRequestBuilder {
        super.saveRecord(isSaveRecord)
        return this
    }

    override fun connectTimeout(timeout: Long, timeUnit: TimeUnit): UploadRequestBuilder {
        super.connectTimeout(timeout, timeUnit)
        return this
    }

    override fun readTimeout(timeout: Long, timeUnit: TimeUnit): UploadRequestBuilder {
        super.readTimeout(timeout, timeUnit)
        return this
    }

    override fun writeTimeout(timeout: Long, timeUnit: TimeUnit): UploadRequestBuilder {
        super.writeTimeout(timeout, timeUnit)
        return this
    }

    fun contentType(contentType: String): UploadRequestBuilder {
        multipartBodyBuilder.setType(contentType.toMediaType())
        return this
    }

    fun addMultipartParam(key: String, value: String): UploadRequestBuilder {
        multipartBodyBuilder.addFormDataPart(key, value)
        requestStringBuilder
            .append(key)
            .append(": ")
            .append(value)
            .append("\n")
        return this
    }

    fun addMultipartFile(key: String, fileName: String, bytes: ByteArray): UploadRequestBuilder {
        multipartBodyBuilder.addFormDataPart(
            key,
            fileName,
            bytes.toRequestBody(mimeType(fileName).toMediaType())
        )
        requestStringBuilder
            .append(key)
            .append(": FILE(")
            .append(fileName)
            .append(", ")
            .append(bytes.fileSizeText()).append(")\n")
        return this
    }

    fun addMultipartFile(key: String, file: File): UploadRequestBuilder {
        multipartBodyBuilder.addFormDataPart(
            key,
            file.name,
            file.asRequestBody(file.mimeType().toMediaType())
        )

        requestStringBuilder
            .append(key)
            .append(": FILE(")
            .append(file.name)
            .append(", ")
            .append(file.fileSizeText()).append(")\n")
        return this
    }

    fun addMultipartFile(key: String, uri: Uri): UploadRequestBuilder {
        var fileName = ""
        var bytes: ByteArray = byteArrayOf()

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            bytes = inputStream.readBytes()
        }
        addMultipartFile(key, fileName, bytes)
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

        return RequestInfo(
            requestBuilder
                .method(
                    httpMethod.code,
                    multipartBodyBuilder.build()
                )
                .build(),
            requestStringBuilder.toString()
        )
    }
}
