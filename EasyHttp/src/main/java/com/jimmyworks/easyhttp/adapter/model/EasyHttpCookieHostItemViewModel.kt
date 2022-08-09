package com.jimmyworks.easyhttp.adapter.model

import com.jimmyworks.easyhttp.database.dto.CookieHostDTO
import java.io.Serializable


class EasyHttpCookieHostItemViewModel(cookieHostDTO: CookieHostDTO) : Serializable {

    val host: String
    val count: Int

    init {
        host = cookieHostDTO.host
        count = cookieHostDTO.cookieCount
    }
}

