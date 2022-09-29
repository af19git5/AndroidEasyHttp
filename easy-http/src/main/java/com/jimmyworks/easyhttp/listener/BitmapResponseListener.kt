package com.jimmyworks.easyhttp.listener

import android.graphics.Bitmap
import com.jimmyworks.easyhttp.exception.HttpException
import okhttp3.Headers

/**
 * 字串回傳監聽器
 *
 * @author Jimmy Kang
 */
interface BitmapResponseListener {

    fun onSuccess(headers: Headers, bitmap: Bitmap)

    fun onProgress(downloadBytes: Long, totalBytes: Long)

    fun onError(e: HttpException)
}
