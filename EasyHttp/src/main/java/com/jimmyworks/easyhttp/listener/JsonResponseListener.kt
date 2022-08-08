package com.jimmyworks.easyhttp.listener

import com.jimmyworks.easyhttp.exception.HttpException
import okhttp3.Headers

/**
 * json回傳監聽器
 *
 * @author Jimmy Kang
 */
interface JsonResponseListener<T> {

    fun onSuccess(headers: Headers, body: T)

    fun onError(e: HttpException)
}
