package com.jimmyworks.easyhttp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*

/**
 * http cookies紀錄
 *
 * @author Jimmy Kang
 */
@Entity(
    tableName = "http_cookies",
    primaryKeys = ["host", "cookie_name"],
    indices = [Index(value = ["host"])]
)
data class HttpCookies(
    @ColumnInfo(name = "host")
    var host: String,

    @ColumnInfo(name = "cookie_name")
    var cookieName: String,

    @ColumnInfo(name = "cookie_value")
    var cookieValue: String,

    @ColumnInfo(name = "expires")
    var expires: Date,

    @ColumnInfo(name = "domain")
    var domain: String,

    @ColumnInfo(name = "path")
    var path: String,

    @ColumnInfo(name = "secure")
    var secure: Boolean,

    @ColumnInfo(name = "http_only")
    var httpOnly: Boolean
) {
    constructor(httpUrl: HttpUrl, cookie: Cookie) : this(
        httpUrl.host,
        cookie.name,
        cookie.value,
        Date(cookie.expiresAt),
        cookie.domain,
        cookie.path,
        cookie.secure,
        cookie.httpOnly
    )

    fun toCookie(): Cookie {
        val builder = Cookie.Builder()
            .name(this.cookieName)
            .value(this.cookieValue)
            .expiresAt(this.expires.time)
            .domain(this.domain)
            .path(this.path)

        if (this.secure) {
            builder.secure()
        }

        if (this.httpOnly) {
            builder.httpOnly()
        }

        return builder.build()
    }
}
