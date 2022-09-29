package com.jimmyworks.easyhttp.listener

import com.jimmyworks.easyhttp.exception.HttpException
import okhttp3.Headers
import java.io.File

/**
 * 下載監聽器
 *
 * @author Jimmy Kang
 */
interface DownloadListener {

    fun onSuccess(headers: Headers, file: File)

    fun onProgress(downloadBytes: Long, totalBytes: Long)

    fun onError(e: HttpException)
}
