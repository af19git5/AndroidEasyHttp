package com.jimmyworks.easyhttp.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Json處理共用
 *
 * @author Jimmy Kang
 */
class JsonUtils {
    companion object {
        private val objectMapper =
            ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)

        /**
         * 物件轉為Json字串
         *
         * @param obj 物件
         * @return json字串
         */
        fun toJson(obj: Any): String {
            return objectMapper.writeValueAsString(obj)
        }
    }
}
