package com.jimmyworks.easyhttp.fragment.model

import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class EasyHttpRecordRequestViewModel : ViewModel() {
    val headers: MutableLiveData<SpannableStringBuilder> = MutableLiveData(SpannableStringBuilder())
    val body: MutableLiveData<String> = MutableLiveData("")
    val haveHeaders: MutableLiveData<Boolean> = MutableLiveData(false)
    val haveBody: MutableLiveData<Boolean> = MutableLiveData(false)
}
