package com.jimmyworks.easyhttp.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期處理共用
 *
 * @author Jimmy Kang
 */
class DateUtils {

    companion object {
        /**
         * 增加秒數(可帶負值)
         *
         * @param date 日期
         * @param second 增加秒數
         * @return 增加後日期
         */
        fun addSecond(date: Date, second: Int): Date {
            val cal: Calendar = dateToCalendar(date)
            cal.add(Calendar.SECOND, second)
            return cal.time
        }

        /**
         * 增加分鐘(可帶負值)
         *
         * @param date 日期
         * @param minute 增加分鐘
         * @return 增加後日期
         */
        fun addMinute(date: Date, minute: Int): Date {
            val cal: Calendar = dateToCalendar(date)
            cal.add(Calendar.MINUTE, minute)
            return cal.time
        }

        /**
         * 增加小時(可帶負值)
         *
         * @param date 日期
         * @param hour 增加小時
         * @return 增加後日期
         */
        fun addHour(date: Date, hour: Int): Date {
            val cal: Calendar = dateToCalendar(date)
            cal.add(Calendar.HOUR, hour)
            return cal.time
        }

        /**
         * 增加日期天數(可帶負值)
         *
         * @param date 日期
         * @param dayCount 增加天數
         * @return 增加後日期
         */
        fun addDay(date: Date, dayCount: Int): Date {
            val cal: Calendar = dateToCalendar(date)
            cal.add(Calendar.DATE, dayCount)
            return cal.time
        }

        /**
         * 增加日期月數(可帶負值)
         *
         * @param date 日期
         * @param monthCount 增加月數
         * @return 增加後日期
         */
        fun addMonth(date: Date, monthCount: Int): Date {
            val cal: Calendar = dateToCalendar(date)
            cal.add(Calendar.MONTH, monthCount)
            return cal.time
        }

        /**
         * 增加日期年數(可帶負值)
         *
         * @param date 日期
         * @param yearCount 增加年數
         * @return 增加後日期
         */
        fun addYear(date: Date, yearCount: Int): Date {
            val cal: Calendar = dateToCalendar(date)
            cal.add(Calendar.YEAR, yearCount)
            return cal.time
        }

        /**
         * 轉換date至Calendar
         *
         * @param date 日期
         * @return Calendar
         */
        fun dateToCalendar(date: Date): Calendar {
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }

        /**
         * 日期字串格式轉換器
         *
         * @param format 輸入日期格式
         * @param convertFormat 輸出日期格式
         * @return 格式化後日期
         */
        @SuppressLint("SimpleDateFormat")
        fun String.dateString(
            format: String, convertFormat: String
        ): String {
            return try {
                val date: Date = SimpleDateFormat(format).parse(this) as Date
                SimpleDateFormat(convertFormat).format(date)
            } catch (e: ParseException) {
                ""
            }
        }

        /**
         * 日期格式轉換器
         *
         * @param format 轉換格式
         * @return 轉換後字串
         */
        @SuppressLint("SimpleDateFormat")
        fun Date.toString(format: String): String {
            return SimpleDateFormat(format).format(this)
        }

        /**
         * 日期字串轉日期格式
         *
         * @param format 日期格式
         * @return 日期
         */
        @SuppressLint("SimpleDateFormat")
        fun String.toDate(format: String): Date? {
            return try {
                SimpleDateFormat(format).parse(this)
            } catch (e: ParseException) {
                null
            }
        }
    }
}
