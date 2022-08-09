package com.jimmyworks.easyhttp.service

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.jimmyworks.easyhttp.entity.RequestInfo
import com.jimmyworks.easyhttp.entity.ResponseInfo
import com.jimmyworks.easyhttp.exception.HttpException
import com.jimmyworks.easyhttp.listener.DownloadListener
import com.jimmyworks.easyhttp.listener.JsonResponseListener
import com.jimmyworks.easyhttp.listener.StringResponseListener
import com.jimmyworks.easyhttp.utils.CommonUtils
import com.jimmyworks.easyhttp.utils.FileUtils
import com.jimmyworks.easyhttp.utils.JsonUtils.Companion.toObject
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*


/**
 * 發送請求服務
 *
 * @author Jimmy Kang
 */
class DoRequestService(
    val context: Context,
    val okHttpClient: OkHttpClient,
    val requestInfo: RequestInfo,
    val isSaveRecord: Boolean
) {

    companion object {
        val callMap: MutableMap<String, MutableList<Call>> = HashMap()
    }

    private fun call(callback: Callback) {
        val call = okHttpClient.newCall(requestInfo.request)
        addCall(requestInfo.request.tag().toString(), call)
        call.enqueue(callback)
    }

    private fun addCall(tag: String, call: Call) {
        val callList = callMap[tag] ?: ArrayList()
        callList.add(call)
        callMap[tag] = callList
    }

    fun getAsString(responseListener: StringResponseListener) {
        getAsString(null, responseListener)
    }

    fun getAsString(charset: Charset?, responseListener: StringResponseListener) {
        val saveRecordService = SaveRecordService(context, requestInfo, Date())
        call(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CommonUtils.runOnUiThread(context) {
                    responseListener.onError(HttpException(requestInfo.url, e))
                }

                if (isSaveRecord) {
                    saveRecordService.save(e.message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    var bodyString = ""
                    if (null != response.body) {
                        bodyString = String(
                            response.body!!.bytes(),
                            charset ?: StandardCharsets.UTF_8
                        )
                    }
                    val responseInfo = ResponseInfo(response, bodyString)

                    CommonUtils.runOnUiThread(context) {
                        if (response.isSuccessful) {
                            responseListener.onSuccess(
                                responseInfo.headers,
                                bodyString
                            )
                        } else {
                            responseListener.onError(
                                HttpException(
                                    requestInfo.request.url.toString(),
                                    responseInfo.code,
                                    bodyString
                                )
                            )
                        }
                    }

                    if (isSaveRecord) {
                        saveRecordService.save(responseInfo)
                    }
                }
            }
        })
    }

    fun <T> getJsonAsObject(clazz: Class<T>, listener: JsonResponseListener<T>) {
        getJsonAsObject(null, clazz, listener)
    }

    fun <T> getJsonAsObject(
        charset: Charset?,
        clazz: Class<T>,
        listener: JsonResponseListener<T>
    ) {
        getAsString(charset, object : StringResponseListener {
            override fun onSuccess(headers: Headers, body: String) {
                val bodyObject: T
                try {
                    bodyObject = body.toObject(clazz)
                } catch (e: Exception) {
                    listener.onError(HttpException(requestInfo.url, e.message!!))
                    return
                }

                listener.onSuccess(headers, bodyObject)
            }

            override fun onError(e: HttpException) {
                listener.onError(e)
            }
        })
    }

    fun <T> getJsonAsObject(type: TypeToken<T>, listener: JsonResponseListener<T>) {
        getJsonAsObject(null, type, listener)
    }

    fun <T> getJsonAsObject(
        charset: Charset?,
        type: TypeToken<T>,
        listener: JsonResponseListener<T>
    ) {
        getAsString(charset, object : StringResponseListener {
            override fun onSuccess(headers: Headers, body: String) {
                val bodyObject: T
                try {
                    bodyObject = body.toObject(type)
                } catch (e: Exception) {
                    listener.onError(HttpException(requestInfo.url, e.message!!))
                    return
                }

                listener.onSuccess(headers, bodyObject)
            }

            override fun onError(e: HttpException) {
                listener.onError(e)
            }
        })
    }

    fun download(file: File, downloadListener: DownloadListener) {
        val saveRecordService = SaveRecordService(context, requestInfo, Date())
        FileUtils.mkdir(file.parentFile!!)

        call(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CommonUtils.runOnUiThread(context) {
                    downloadListener.onError(HttpException(requestInfo.url, e))
                }

                if (isSaveRecord) {
                    saveRecordService.save(e.message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        val body = response.body ?: throw HttpException(
                            requestInfo.url,
                            "Response body is empty."
                        )
                        val contentLength = body.contentLength()
                        val inputStream = body.byteStream()

                        val buf = ByteArray(1024)
                        var sum: Long = 0
                        var len = 0

                        FileOutputStream(file).use { outputStream: FileOutputStream ->
                            while (len != -1) {
                                outputStream.write(buf, 0, len)
                                sum += len.toLong()
                                val finalSum = sum
                                if (contentLength == -1L) {
                                    len = inputStream.read(buf)
                                    continue
                                }
                                CommonUtils.runOnUiThread(context) {
                                    downloadListener.onProgress(
                                        finalSum, contentLength
                                    )
                                }
                                len = inputStream.read(buf)
                            }
                        }

                        CommonUtils.runOnUiThread(context) {
                            downloadListener.onSuccess(
                                response.headers,
                                file
                            )
                        }

                        if (isSaveRecord) {
                            saveRecordService.save(ResponseInfo(response, file))
                        }
                    } catch (e: HttpException) {
                        saveRecordService.save(e.message)
                        CommonUtils.runOnUiThread(context) {
                            downloadListener.onError(e)
                        }
                    }
                }
            }
        })
    }
}
