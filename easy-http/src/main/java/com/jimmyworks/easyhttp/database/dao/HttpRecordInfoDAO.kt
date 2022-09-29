package com.jimmyworks.easyhttp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo

@Dao
interface HttpRecordInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(httpRecordInfo: HttpRecordInfo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg httpRecordInfo: HttpRecordInfo): LongArray

    @Delete
    fun delete(vararg httpRecordInfo: HttpRecordInfo)

    @Query("DELETE FROM http_record_info WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM http_record_info")
    fun deleteAll()

    @Query("SELECT * FROM http_record_info ORDER BY send_time DESC")
    fun findAllRecord(): LiveData<List<HttpRecordInfo>>

    @Query("SELECT * FROM http_record_info WHERE id = :id")
    fun findById(id: Long): LiveData<HttpRecordInfo>

    @Query("SELECT * FROM http_record_info WHERE url LIKE :url ORDER BY send_time DESC")
    fun findByUrl(url: String): LiveData<List<HttpRecordInfo>>

    @Query("SELECT * FROM http_record_info WHERE id < (SELECT max(id) FROM http_record_info) - :limitCount")
    fun findOverRecord(limitCount: Int): List<HttpRecordInfo>
}
