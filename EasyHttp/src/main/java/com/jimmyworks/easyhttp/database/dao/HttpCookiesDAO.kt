package com.jimmyworks.easyhttp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jimmyworks.easyhttp.database.entity.HttpCookies
import java.util.*

@Dao
interface HttpCookiesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(httpCookies: HttpCookies): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg httpCookies: HttpCookies): LongArray

    @Query("SELECT * FROM http_cookies WHERE host = :host ORDER BY cookie_name ASC")
    fun findByHost(host: String): List<HttpCookies>

    @Query("DELETE FROM http_cookies")
    fun deleteAll()

    @Query("DELETE FROM http_cookies WHERE expires < :expires")
    fun deleteByExpire(expires: Date)
}
