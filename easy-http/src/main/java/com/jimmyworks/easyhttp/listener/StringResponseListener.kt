package com.jimmyworks.easyhttp.listener

import com.jimmyworks.easyhttp.exception.HttpException
import okhttp3.Headers

/**
 * 字串回傳監聽器
 *
 * @author Jimmy Kang
 */
interface StringResponseListener {

    fun onSuccess(headers: Headers, body: String)

    fun onError(e: HttpException)
}
