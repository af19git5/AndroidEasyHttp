package com.jimmyworks.easyhttp.listener

import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo

/**
 * 紀錄監聽器
 *
 * @author Jimmy Kang
 */
interface RecordListener {

    fun onDoneRecord(httpRecordInfo: HttpRecordInfo)
}
