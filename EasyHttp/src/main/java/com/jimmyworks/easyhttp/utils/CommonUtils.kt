package com.jimmyworks.easyhttp.utils

import android.content.Context
import android.os.Handler
import android.view.View
import java.io.File
import kotlin.math.abs

/**
 *
 *
 * @author Jimmy Kang
 */
class CommonUtils {
    companion object {

        /**
         * 執行在主執行緒
         *
         * @param context Context
         * @param runnable 運行作業
         */
        fun runOnUiThread(context: Context, runnable: Runnable) {
            val mainHandler = Handler(context.mainLooper)
            mainHandler.post(runnable)
        }

        /**
         * 取得快取目錄
         *
         * @param context Context
         * @param dirName 目錄名稱
         * @return 快取目錄
         */
        fun getDiskCacheDir(context: Context, dirName: String): File {
            return File(context.cacheDir, dirName)
        }

        /** 最近一次點擊時間  */
        private var mLastClickTime: Long = 0

        /** 最近一次點擊元件id  */
        private var mLastClickViewId = 0

        /**
         * 是否快速點擊
         *
         * @param v 點擊的元件
         * @param intervalMillis 時間間隔
         * @return true:是，false:不是
         */
        fun isFastDoubleClick(v: View, intervalMillis: Long): Boolean {
            val viewId: Int = v.id
            val time = System.currentTimeMillis()
            val timeInterval: Long = abs(time - mLastClickTime)
            return if (timeInterval < intervalMillis && viewId == mLastClickViewId) {
                true
            } else {
                mLastClickTime = time
                mLastClickViewId = viewId
                false
            }
        }
    }
}
