package com.jimmyworks.easyhttp.fragment.model

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import com.jimmyworks.easyhttp.type.HttpStatus
import com.jimmyworks.easyhttp.utils.DateUtils.Companion.toString
import java.text.DecimalFormat


class EasyHttpRecordInfoViewModel : ViewModel() {

    val url: MutableLiveData<String> = MutableLiveData("")
    val method: MutableLiveData<String> = MutableLiveData("")
    val statusCode: MutableLiveData<String> = MutableLiveData("")
    val sendTime: MutableLiveData<String> = MutableLiveData("")
    val spendTime: MutableLiveData<String> = MutableLiveData("")
    val isError: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorMessage: MutableLiveData<String> = MutableLiveData("")

    fun setHttpRecordInfo(httpRecordInfo: HttpRecordInfo) {
        url.value = httpRecordInfo.url
        method.value = httpRecordInfo.method.code

        if (null == httpRecordInfo.statusCode) {
            this.statusCode.value = "None"
        } else {
            val httpStatus = HttpStatus.toHttpStatus(httpRecordInfo.statusCode!!)
            this.statusCode.value = httpStatus.value.toString() + " " + httpStatus.reasonPhrase
        }

        sendTime.value = httpRecordInfo.sendTime.toString("yyyy-MM-dd HH:mm:ss.SSS")
        if (null != httpRecordInfo.receiveTime) {
            val spendTime: Long = (httpRecordInfo.receiveTime!!.time - httpRecordInfo.sendTime.time)

            val spendSecond = spendTime.toDouble() / 1000

            val decimalFormat = DecimalFormat("###.##")

            if (spendSecond >= 1) {
                this.spendTime.value = decimalFormat.format(spendSecond) + " s"
            } else {
                this.spendTime.value = "$spendTime ms"
            }
        } else {
            this.spendTime.value = "-"
        }

        if (!TextUtils.isEmpty(httpRecordInfo.errorMessage)) {
            this.isError.value = true
            this.errorMessage.value = httpRecordInfo.errorMessage
        }
    }
}
