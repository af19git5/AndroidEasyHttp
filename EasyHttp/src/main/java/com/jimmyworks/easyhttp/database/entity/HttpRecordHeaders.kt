package com.jimmyworks.easyhttp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * http headers發送紀錄
 *
 * @author Jimmy Kang
 */
@Entity(tableName = "http_record_headers", indices = [Index(value = ["record_id"])])
data class HttpRecordHeaders(

    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "record_id")
    var recordId: Long?,

    @ColumnInfo(name = "is_response")
    var isResponse: Boolean,

    @ColumnInfo(name = "header")
    var header: String,

    @ColumnInfo(name = "header_value")
    var headerValue: String,
) : Serializable
