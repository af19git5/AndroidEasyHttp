package com.jimmyworks.easyhttp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jimmyworks.easyhttp.type.HttpMethod
import java.io.Serializable
import java.util.*

/**
 * http傳輸紀錄
 *
 * @author Jimmy Kang
 */
@Entity(tableName = "http_record_info")
data class HttpRecordInfo(

    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "method")
    var method: HttpMethod,

    @ColumnInfo(name = "request_content_type")
    var requestContentType: String?,

    @ColumnInfo(name = "response_content_type")
    var responseContentType: String?,

    @ColumnInfo(name = "status_code")
    var statusCode: Int?,

    @ColumnInfo(name = "error_message")
    var errorMessage: String?,

    @ColumnInfo(name = "send_time")
    var sendTime: Date,

    @ColumnInfo(name = "receive_time")
    var receiveTime: Date?,
) : Serializable
