package com.jimmyworks.easyhttp.database.repository

import android.content.Context
import com.jimmyworks.easyhttp.database.HttpRecordDatabase
import com.jimmyworks.easyhttp.database.dao.HttpCookiesDAO
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

    fun clearExpireCookies() {
        HttpRecordDatabase.databaseWriteExecutor.execute { httpCookiesDAO.deleteByExpire(Date()) }
    }

    fun clearCookies() {
        HttpRecordDatabase.databaseWriteExecutor.execute(httpCookiesDAO::deleteAll)
    }
}
