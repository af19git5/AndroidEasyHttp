package com.jimmyworks.easyhttp.adapter.model

import android.text.TextUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import com.jimmyworks.easyhttp.type.HttpStatus
import com.jimmyworks.easyhttp.type.HttpStatus.Series
import com.jimmyworks.easyhttp.utils.DateUtils.Companion.toString
import java.io.Serializable


class EasyHttpRecordItemViewModel(httpRecordInfo: HttpRecordInfo) : Serializable {

    val id: Long

    val method: String

    val url: String

    val sendTime: String

    val isError: Boolean

    val httpRecordInfo: HttpRecordInfo

    init {
        this.httpRecordInfo = httpRecordInfo
        id = httpRecordInfo.id!!
        method = httpRecordInfo.method.code
        url = httpRecordInfo.url
        sendTime = httpRecordInfo.sendTime.toString("yyyy-MM-dd HH:mm:ss.SSS")
        isError = if (null != httpRecordInfo.statusCode) {
            HttpStatus.toHttpStatus(httpRecordInfo.statusCode!!).series != Series.SUCCESSFUL
        } else {
            false
        }
    }
}

@BindingAdapter("setHttpStatus")
fun setHttpStatus(textView: TextView, httpRecordInfo: HttpRecordInfo) {
    var httpStatus: HttpStatus? = null
    if (null != httpRecordInfo.statusCode) {
        httpStatus = HttpStatus.toHttpStatus(httpRecordInfo.statusCode!!)
    }
    val status: String
    val statusColor: Int
    if (null != httpStatus) {
        status = httpStatus.value.toString() + " " + httpStatus.reasonPhrase
        statusColor = when (httpStatus.series) {
            Series.SUCCESSFUL -> R.color.easyHttpStatusColorSuccess
            Series.INFORMATIONAL, Series.REDIRECTION -> R.color.easyHttpStatusColorWarring
            else -> R.color.easyHttpStatusColorError
        }
    } else {
        if (null != httpRecordInfo.receiveTime) {
            status = "None"
            statusColor = R.color.easyHttpStatusColorError
        } else {
            if (TextUtils.isEmpty(httpRecordInfo.errorMessage)) {
                status = "Wait"
                statusColor = R.color.easyHttpStatusColorWarring
            } else {
                status = "None"
                statusColor = R.color.easyHttpStatusColorError
            }
        }
    }
    textView.text = status
    textView.setTextColor(ContextCompat.getColor(textView.context, statusColor))
}
