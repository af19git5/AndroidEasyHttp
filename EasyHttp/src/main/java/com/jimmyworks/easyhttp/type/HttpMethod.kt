package com.jimmyworks.easyhttp.type

import androidx.room.TypeConverter
import com.jimmyworks.easyhttp.exception.TypeNotFoundException
import java.util.*

/**
 * http傳輸格式
 *
 * @author Jimmy Kang
 */
enum class HttpMethod(var code: String) {

    GET("GET"),

    HEAD("HEAD"),

    POST("POST"),

    PUT("PUT"),

    DELETE("DELETE"),

    CONNECT("CONNECT"),

    OPTIONS("OPTIONS"),

    TRACE("TRACE"),

    PATCH("PATCH");

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
