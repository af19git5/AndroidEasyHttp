package com.jimmyworks.easyhttp.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.underscore.Json
import com.github.underscore.U
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import java.io.File
import java.util.*
import kotlin.math.abs


/**
 * 常用公用方法
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

        fun View.onSingleClick(interval: Int = 500, onClickListener: View.OnClickListener) {
            setOnClickListener {
                val viewId: Int = it.id
                val time = System.currentTimeMillis()
                val timeInterval: Long = abs(time - mLastClickTime)
                if (timeInterval >= interval || viewId != mLastClickViewId) {
                    mLastClickTime = time
                    mLastClickViewId = viewId
                    onClickListener.onClick(it)
                }
            }
        }

        fun String.prettify(contentType: String?): String {
            contentType ?: return this
            return prettify(contentType.toMediaType())
        }

        fun String.prettify(mediaType: MediaType?): String {
            mediaType ?: return this
            return try {
                when (mediaType.subtype.lowercase(Locale.ENGLISH)) {
                    "json" -> {
                        U.formatJson(this, Json.JsonStringBuilder.Step.FOUR_SPACES)
                    }
                    "xml" -> {
                        U.formatXml(this)
                    }
                    else -> {
                        this
                    }
                }
            } catch (e: Exception) {
                this
            }
        }

    }
}

@BindingAdapter("app:imageBitmap")
fun imageBitmap(imageView: ImageView, bitmap: Bitmap?) {
    imageView.setImageBitmap(bitmap)
}
