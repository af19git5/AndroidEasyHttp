package com.jimmyworks.easyhttp.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Json處理共用
 *
 * @author Jimmy Kang
 */
class JsonUtils {
    companion object {

        /**
         * 物件轉為Json字串
         *
         * @param obj 物件
         * @return json字串
         */
        fun toJson(obj: Any): String {
            return Gson().toJson(obj)
        }

        /**
         * 轉換為物件
         *
         * @param clazz 物件類別
         * @return 物件
         */
        fun <T> String.toObject(clazz: Class<T>): T {
            return Gson().fromJson(this, clazz)
        }

        /**
         * 轉換為物件
         *
         * @param type 物件類別
         * @return 物件
         */
        fun <T> String.toObject(type: TypeToken<T>): T {
            return Gson().fromJson(this, type.type)
        }
    }
}
