package com.jimmyworks.easyhttp.activity.model

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jimmyworks.easyhttp.database.dto.CookieHostDTO
import com.jimmyworks.easyhttp.database.repository.HttpCookiesRepository


class EasyHttpCookiesViewModel(application: Application) : AndroidViewModel(application) {

    private val httpCookiesRepository: HttpCookiesRepository

    val cookieHostList: LiveData<List<CookieHostDTO>>
    val noRecord = ObservableField(true)

    init {
        httpCookiesRepository = HttpCookiesRepository(application)
        cookieHostList = httpCookiesRepository.findHostAndCount()
    }
}
