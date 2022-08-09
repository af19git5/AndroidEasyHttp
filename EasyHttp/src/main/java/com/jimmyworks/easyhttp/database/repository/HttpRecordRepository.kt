package com.jimmyworks.easyhttp.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jimmyworks.easyhttp.EasyHttpConfig
import com.jimmyworks.easyhttp.database.HttpRecordDatabase
import com.jimmyworks.easyhttp.database.dao.HttpCookiesDAO
import com.jimmyworks.easyhttp.database.dao.HttpRecordHeadersDAO
import com.jimmyworks.easyhttp.database.dao.HttpRecordInfoDAO
import com.jimmyworks.easyhttp.database.entity.HttpRecordHeaders
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import com.jimmyworks.easyhttp.listener.RecordListener
import com.jimmyworks.easyhttp.utils.CommonUtils
import com.jimmyworks.easyhttp.utils.FileUtils
import java.io.File


/**
 * Http紀錄Repository
 *
 * @author Jimmy Kang
 */
class HttpRecordRepository(context: Context) {

    private val context: Context
    private val httpRecordInfoDAO: HttpRecordInfoDAO
    private val httpRecordHeadersDAO: HttpRecordHeadersDAO
    private val httpCookiesDAO: HttpCookiesDAO

    private val requestDir: File =
        CommonUtils.getDiskCacheDir(context, EasyHttpConfig.REQUEST_DIR_NAME)
    private val responseDir: File =
        CommonUtils.getDiskCacheDir(context, EasyHttpConfig.RESPONSE_DIR_NAME)

    init {
        val db: HttpRecordDatabase = HttpRecordDatabase.getDatabase(context)
        this.context = context
        this.httpRecordInfoDAO = db.httpRecordInfoDAO()
        this.httpRecordHeadersDAO = db.httpRecordHeadersDAO()
        this.httpCookiesDAO = db.httpCookiesDAO()
    }

    fun insert(
        httpRecordInfo: HttpRecordInfo,
        headerList: List<HttpRecordHeaders>
    ): Long {
        clearMore()
        httpRecordInfo.id = httpRecordInfoDAO.insert(httpRecordInfo)
        for (header in headerList) {
            header.recordId = httpRecordInfo.id!!
        }
        httpRecordHeadersDAO.insert(*headerList.toTypedArray())

        return httpRecordInfo.id!!
    }

    fun insert(
        httpRecordInfo: HttpRecordInfo,
        headerList: List<HttpRecordHeaders>,
        recordListener: RecordListener?
    ) {
        clearMore()
        HttpRecordDatabase.databaseWriteExecutor.execute {
            insert(httpRecordInfo, headerList)
            CommonUtils.runOnUiThread(context) {
                recordListener?.onDoneRecord(httpRecordInfo)
            }
        }
    }

    private fun clearMore() {
        HttpRecordDatabase.databaseWriteExecutor.execute {
            val httpRecordInfoList =
                httpRecordInfoDAO.findOverRecord(EasyHttpConfig.MAX_RECORD_COUNT)
            for (httpRecordInfo in httpRecordInfoList) {
                val id = httpRecordInfo.id ?: 0
                httpRecordHeadersDAO.deleteByRecordId(id)
                httpRecordInfoDAO.delete(httpRecordInfo)
                FileUtils.delete(File(requestDir, id.toString()))
                FileUtils.delete(File(responseDir, id.toString()))
            }
        }
    }

    fun clearALL() {
        HttpRecordDatabase.databaseWriteExecutor.execute {
            httpRecordHeadersDAO.deleteAll()
            httpRecordInfoDAO.deleteAll()
            FileUtils.deleteDir(requestDir)
            FileUtils.deleteDir(responseDir)
        }
    }

    fun findByUrl(url: String): LiveData<List<HttpRecordInfo>> {
        return httpRecordInfoDAO.findByUrl("%$url%")
    }

    fun findById(id: Long): LiveData<HttpRecordInfo> {
        return httpRecordInfoDAO.findById(id)
    }

    fun findRequestHeadersByRecordId(id: Long): LiveData<List<HttpRecordHeaders>> {
        return httpRecordHeadersDAO.findByRecordIdAndIsResponse(id, false)
    }

    fun findResponseHeadersByRecordId(id: Long): LiveData<List<HttpRecordHeaders>> {
        return httpRecordHeadersDAO.findByRecordIdAndIsResponse(id, true)
    }
}
