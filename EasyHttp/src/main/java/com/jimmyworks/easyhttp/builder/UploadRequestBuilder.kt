package com.jimmyworks.easyhttp.builder

import android.content.Context
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

/**
 * 檔案上傳建構器
 *
 * @author Jimmy Kang
 */
open class UploadRequestBuilder : RequestBuilder {

    private val multipartBodyBuilder = MultipartBody.Builder()
    private val requestStringBuilder = StringBuilder("Form-data\n\n")

    constructor(context: Context, url: String) : super(context, url, HttpMethod.POST)

    constructor(context: Context, url: String, httpMethod: HttpMethod) : super(
        context,
        url,
        httpMethod
    )

    fun addMultipartParameter(key: String, value: String): UploadRequestBuilder {
        multipartBodyBuilder.addFormDataPart(key, value)
        requestStringBuilder
            .append(key)
            .append(":")
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
            .append(":FILE(")
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
            .append(":FILE(")
            .append(file.name)
            .append(", ")
            .append(file.fileSizeText()).append(")\n")
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
