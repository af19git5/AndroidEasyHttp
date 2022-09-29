package com.jimmyworks.easyhttp.activity.model

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import com.jimmyworks.easyhttp.database.repository.HttpRecordRepository


class EasyHttpRecordViewModel(application: Application) : AndroidViewModel(application) {

    private val httpRecordRepository: HttpRecordRepository

    val recordList: LiveData<List<HttpRecordInfo>>
    val searchText: MutableLiveData<String>
    val noRecord = ObservableField(true)

    init {
        httpRecordRepository = HttpRecordRepository(application)
        searchText = MutableLiveData("")
        recordList = Transformations.switchMap(searchText, (httpRecordRepository::findByUrl))
    }

    fun clearRecord() {
        httpRecordRepository.clearALL()
    }
}
