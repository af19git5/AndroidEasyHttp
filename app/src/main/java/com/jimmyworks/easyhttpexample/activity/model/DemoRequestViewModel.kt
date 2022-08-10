package com.jimmyworks.easyhttpexample.activity.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttpexample.data.Header


class DemoRequestViewModel(isUpload: Boolean) : ViewModel() {

    var url: String
    var body = ""

    val isUpload = MutableLiveData(isUpload)
    val method: MutableLiveData<HttpMethod>
    val headerList: MutableList<Header>
    val response = MutableLiveData("")

    init {
        headerList = ArrayList()

        if (isUpload) {
            url = ""
            method = MutableLiveData(HttpMethod.POST)
            headerList.add(Header("Content-Type", "multipart/form-data"))
        } else {
            url = "https://google.com"
            method = MutableLiveData(HttpMethod.GET)
            headerList.add(Header("Content-Type", "application/json"))
        }
    }
}
