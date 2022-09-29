package com.jimmyworks.easyhttp.database.dto

import androidx.room.ColumnInfo
import java.io.Serializable


data class CookieHostDTO(
    @ColumnInfo(name = "host")
    var host: String,

    @ColumnInfo(name = "cookieCount")
    var cookieCount: Int

) : Serializable

