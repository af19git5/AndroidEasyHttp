package com.jimmyworks.easyhttp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jimmyworks.easyhttp.database.entity.HttpRecordHeaders

@Dao
interface HttpRecordHeadersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(httpRecordHeaders: HttpRecordHeaders): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg httpRecordHeaders: HttpRecordHeaders): LongArray

    @Delete
    fun delete(vararg httpRecordHeaders: HttpRecordHeaders)

    @Query("DELETE FROM http_record_headers WHERE record_id = :recordId")
    fun deleteByRecordId(recordId: Long)

    @Query("DELETE FROM http_record_headers")
    fun deleteAll()

    @Query("SELECT * FROM http_record_headers WHERE record_id = :recordId ORDER BY header ASC")
    fun findByRecordId(recordId: Long): LiveData<List<HttpRecordHeaders>>

    @Query("SELECT * FROM http_record_headers WHERE record_id = :recordId AND is_response = :isResponse ORDER BY header ASC")
    fun findByRecordIdAndIsResponse(
        recordId: Long,
        isResponse: Boolean
    ): LiveData<List<HttpRecordHeaders>>
}
