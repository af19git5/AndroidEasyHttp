package com.jimmyworks.easyhttp.database.converter

import androidx.room.TypeConverter
import java.util.*

/**
 * Date轉換器
 *
 * @author Jimmy Kang
 */
class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        val a = dateLong ?: return null
        return Date(a)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}
