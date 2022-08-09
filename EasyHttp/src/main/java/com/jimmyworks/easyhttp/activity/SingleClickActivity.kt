package com.jimmyworks.easyhttp.activity

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

abstract class SingleClickActivity : AppCompatActivity(), View.OnClickListener {

    abstract fun onSingleClick(view: View)

    val intervalMillis = 1000

    override fun onClick(view: View) {
        val viewId: Int = view.id
        val time = System.currentTimeMillis()
        val timeInterval: Long = abs(time - mLastClickTime)
        if (timeInterval >= intervalMillis || viewId != mLastClickViewId) {
            mLastClickTime = time
            mLastClickViewId = viewId
            onSingleClick(view)
        }
    }

    companion object {
        /** 最近一次點擊時間  */
        private var mLastClickTime: Long = 0

        /** 最近一次點擊元件id  */
        private var mLastClickViewId = 0
    }
}
