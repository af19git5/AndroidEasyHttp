package com.jimmyworks.easyhttp.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jimmyworks.easyhttp.database.HttpRecordDatabase
import com.jimmyworks.easyhttp.database.dao.HttpCookiesDAO
import com.jimmyworks.easyhttp.database.dto.CookieHostDTO
import com.jimmyworks.easyhttp.database.entity.HttpCookies
import java.util.*

/**
 * http cookie Repository
 *
 * @author Jimmy Kang
 */
class HttpCookiesRepository(context: Context) {

    private val context: Context
    private val httpCookiesDAO: HttpCookiesDAO

    init {
        val db: HttpRecordDatabase = HttpRecordDatabase.getDatabase(context)
        this.context = context
        this.httpCookiesDAO = db.httpCookiesDAO()
    }

    fun insert(cookieList: List<HttpCookies>) {
        HttpRecordDatabase.databaseWriteExecutor.execute {
            httpCookiesDAO.insert(*cookieList.toTypedArray())
        }
    }

    fun findByHost(host: String): List<HttpCookies> {
        return httpCookiesDAO.findByHost(host)
    }

    fun findLiveByHost(host: String): LiveData<List<HttpCookies>> {
        return httpCookiesDAO.findLiveByHost(host)
    }

    fun findHostAndCount(): LiveData<List<CookieHostDTO>> {
        return httpCookiesDAO.findHostAndCount()
    }

    fun clearExpireCookies() {
        HttpRecordDatabase.databaseWriteExecutor.execute { httpCookiesDAO.deleteByExpire(Date()) }
    }

    fun clearCookies() {
        HttpRecordDatabase.databaseWriteExecutor.execute(httpCookiesDAO::deleteAll)
    }

    fun clearCookiesByHost(host: String) {
        HttpRecordDatabase.databaseWriteExecutor.execute { httpCookiesDAO.deleteByHost(host) }
    }

    fun clearCookiesByHostAndName(host: String, name: String) {
        HttpRecordDatabase.databaseWriteExecutor.execute {
            httpCookiesDAO.deleteByHostAndName(
                host,
                name
            )
        }
    }
}
