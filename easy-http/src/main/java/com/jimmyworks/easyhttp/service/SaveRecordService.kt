package com.jimmyworks.easyhttp.service

import android.content.Context
import android.text.TextUtils
import com.jimmyworks.easyhttp.EasyHttpConfig
import com.jimmyworks.easyhttp.database.entity.HttpRecordHeaders
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import com.jimmyworks.easyhttp.database.repository.HttpRecordRepository
import com.jimmyworks.easyhttp.entity.RequestInfo
import com.jimmyworks.easyhttp.entity.ResponseInfo
import com.jimmyworks.easyhttp.listener.RecordListener
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttp.utils.CommonUtils
import com.jimmyworks.easyhttp.utils.FileUtils
import java.io.File
import java.util.*

/**
 * 儲存傳送紀錄服務
 *
 * @author Jimmy Kang
 */
class SaveRecordService(
    context: Context,
    private val requestInfo: RequestInfo,
    private val sendTime: Date
) {
    private val httpRecordRepository: HttpRecordRepository = HttpRecordRepository(context)
    private val requestDir: File =
        CommonUtils.getDataDir(context, EasyHttpConfig.REQUEST_DIR_NAME)
    private val responseDir: File =
        CommonUtils.getDataDir(context, EasyHttpConfig.RESPONSE_DIR_NAME)

    init {
        if (!requestDir.exists()) {
            requestDir.mkdir()
        }

        if (!responseDir.exists()) {
            responseDir.mkdir()
        }
    }

    fun save(responseInfo: ResponseInfo) {
        val httpRecordInfo = HttpRecordInfo(
            null,
            requestInfo.url,
            HttpMethod.toHttpMethod(requestInfo.request.method),
            requestInfo.contentType,
            responseInfo.contentType,
            responseInfo.code,
            null,
            sendTime,
            Date()
        )

        val headerRecordList: MutableList<HttpRecordHeaders> = ArrayList()
        // request header
        if (!TextUtils.isEmpty(requestInfo.contentType)) {
            headerRecordList.add(
                HttpRecordHeaders(
                    null,
                    null,
                    false,
                    "Content-Type",
                    requestInfo.contentType!!
                )
            )
        }
        for (header in requestInfo.headers) {
            if (header.first.equals("Content-Type", true)) {
                continue
            }
            headerRecordList.add(
                HttpRecordHeaders(
                    null,
                    null,
                    false,
                    header.first,
                    header.second
                )
            )
        }
        // response header
        for (header in responseInfo.headers) {
            headerRecordList.add(
                HttpRecordHeaders(
                    null,
                    null,
                    true,
                    header.first,
                    header.second
                )
            )
        }

        // 執行儲存記錄
        val id = httpRecordRepository.insert(httpRecordInfo, headerRecordList)
        saveRequest(id)
        saveResponse(id, responseInfo)
    }

    fun save(errorMessage: String) {
        val httpRecordInfo = HttpRecordInfo(
            null,
            requestInfo.url,
            HttpMethod.toHttpMethod(requestInfo.request.method),
            requestInfo.contentType,
            null,
            null,
            errorMessage,
            sendTime,
            null
        )

        val headerRecordList: MutableList<HttpRecordHeaders> = ArrayList()
        // request header
        if (!TextUtils.isEmpty(requestInfo.contentType)) {
            headerRecordList.add(
                HttpRecordHeaders(
                    null,
                    null,
                    false,
                    "Content-Type",
                    requestInfo.contentType!!
                )
            )
        }
        for (header in requestInfo.headers) {
            if (header.first.equals("Content-Type", true)) {
                continue
            }
            headerRecordList.add(
                HttpRecordHeaders(
                    null,
                    null,
                    false,
                    header.first,
                    header.second
                )
            )
        }

        // 執行儲存記錄
        httpRecordRepository.insert(
            httpRecordInfo,
            headerRecordList,
            object : RecordListener {
                override fun onDoneRecord(httpRecordInfo: HttpRecordInfo) {
                    saveRequest(httpRecordInfo.id!!)
                }
            })
    }

    private fun saveRequest(id: Long) {
        if (null != requestInfo.requestString) {
            FileUtils.writeTextFile(requestDir, id.toString(), requestInfo.requestString!!)
        }
    }

    private fun saveResponse(id: Long, responseInfo: ResponseInfo) {
        if (responseInfo.body is File) {
            FileUtils.copy(responseInfo.body, File(responseDir, id.toString()))
        } else if (responseInfo.body is String) {
            FileUtils.writeTextFile(responseDir, id.toString(), responseInfo.body)
        }
    }
}
