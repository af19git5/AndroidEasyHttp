package com.jimmyworks.easyhttpexample.adapter.model

import com.jimmyworks.easyhttpexample.data.Header
import java.io.Serializable


class HeadersViewModel(header: Header) : Serializable {

    val key: String
    val value: String

    init {
        key = header.key
        value = header.value
    }
}

