package com.jimmyworks.easyhttp.other

import android.content.Context
import com.jimmyworks.easyhttp.database.entity.HttpCookies
import com.jimmyworks.easyhttp.database.repository.HttpCookiesRepository
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class EasyHttpCookieJar(context: Context) : CookieJar {

    val httpCookiesRepository = HttpCookiesRepository(context)

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        httpCookiesRepository.clearExpireCookies()
        val cookieList: MutableList<Cookie> = ArrayList()
        val cookieInfoList: List<HttpCookies> = httpCookiesRepository.findByHost(url.host)
        for (cookieInfo in cookieInfoList) {
            cookieList.add(cookieInfo.toCookie())
        }
        return cookieList
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val cookieInfoList: MutableList<HttpCookies> = ArrayList()
        for (cookie in cookies) {
            cookieInfoList.add(HttpCookies(url, cookie))
        }
        httpCookiesRepository.insert(cookieInfoList)
    }
}
