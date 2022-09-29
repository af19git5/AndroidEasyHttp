package com.jimmyworks.easyhttp.fragment.model

import android.graphics.Bitmap
import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class EasyHttpRecordResponseViewModel : ViewModel() {
    val headers: MutableLiveData<SpannableStringBuilder> = MutableLiveData(SpannableStringBuilder())
    val body: MutableLiveData<String> = MutableLiveData("")
    val haveHeaders: MutableLiveData<Boolean> = MutableLiveData(false)
    val haveBody: MutableLiveData<Boolean> = MutableLiveData(false)
    val imageBody: MutableLiveData<Bitmap> = MutableLiveData(null)
    val showImage: MutableLiveData<Boolean> = MutableLiveData(false)
}
