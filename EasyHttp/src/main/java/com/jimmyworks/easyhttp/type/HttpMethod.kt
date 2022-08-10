package com.jimmyworks.easyhttp.type

import androidx.room.TypeConverter
import com.jimmyworks.easyhttp.exception.TypeNotFoundException

/**
 * http傳輸格式
 *
 * @author Jimmy Kang
 */
enum class HttpMethod(val code: String, val haveBody: Boolean) {

    GET("GET", false),

    HEAD("HEAD", false),

    POST("POST", true),

    PUT("PUT", true),

    DELETE("DELETE", true),

    PATCH("PATCH", true);

    companion object {
        @TypeConverter
        fun toHttpMethod(code: String): HttpMethod {
            for (httpMethod in values()) {
                if (httpMethod.code.equals(code, true)) {
                    return httpMethod
                }
            }
            throw TypeNotFoundException("http method not found")
        }

        @TypeConverter
        fun toCode(httpMethod: HttpMethod): String {
            return httpMethod.code
        }
    }
}
