package com.jimmyworks.easyhttp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jimmyworks.easyhttp.database.converter.DateConverter
import com.jimmyworks.easyhttp.database.dao.HttpCookiesDAO
import com.jimmyworks.easyhttp.database.dao.HttpRecordHeadersDAO
import com.jimmyworks.easyhttp.database.dao.HttpRecordInfoDAO
import com.jimmyworks.easyhttp.database.entity.HttpCookies
import com.jimmyworks.easyhttp.database.entity.HttpRecordHeaders
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(
    entities = [
        HttpRecordInfo::class,
        HttpRecordHeaders::class,
        HttpCookies::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [DateConverter::class])
abstract class HttpRecordDatabase : RoomDatabase() {
    abstract fun httpRecordInfoDAO(): HttpRecordInfoDAO

    abstract fun httpRecordHeadersDAO(): HttpRecordHeadersDAO

    abstract fun httpCookiesDAO(): HttpCookiesDAO

    companion object {

        private var INSTANCE: HttpRecordDatabase? = null

        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(4)

        fun getDatabase(context: Context): HttpRecordDatabase {
            if (null == INSTANCE) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HttpRecordDatabase::class.java,
                    "http_record"
                ).build()
            }
            return INSTANCE as HttpRecordDatabase
        }
    }
}
