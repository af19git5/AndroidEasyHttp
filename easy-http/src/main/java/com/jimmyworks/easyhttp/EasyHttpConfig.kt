package com.jimmyworks.easyhttp

/**
 * 設定參數
 *
 * @author Jimmy Kang
 */
object EasyHttpConfig {

    const val MAX_CACHE_SIZE: Long = 10 * 1024 * 1024

    const val CACHE_DIR_NAME: String = "cache_easy_http"

    const val IMAGE_CACHE_DIR_NAME: String = "image_cache_easy_http"

    const val REQUEST_DIR_NAME: String = "easy_http_request_record"

    const val RESPONSE_DIR_NAME: String = "easy_http_response_record"

    const val MAX_RECORD_COUNT: Int = 1000
}
