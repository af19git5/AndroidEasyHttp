package com.jimmyworks.easyhttp

import android.content.Context
import android.content.Intent
import com.jimmyworks.easyhttp.activity.EasyHttpRecordActivity
import com.jimmyworks.easyhttp.builder.PostRequestBuilder
import com.jimmyworks.easyhttp.builder.RequestBuilder
import com.jimmyworks.easyhttp.builder.UploadRequestBuilder
import com.jimmyworks.easyhttp.database.repository.HttpCookiesRepository
import com.jimmyworks.easyhttp.service.DoRequestService
import com.jimmyworks.easyhttp.type.HttpMethod


class EasyHttp {

    companion object {
        @JvmStatic
        fun get(context: Context, url: String): RequestBuilder {
            return RequestBuilder(context, url)
        }

        @JvmStatic
        fun head(context: Context, url: String): RequestBuilder {
            return RequestBuilder(context, url, HttpMethod.HEAD)
        }

        @JvmStatic
        fun post(context: Context, url: String): PostRequestBuilder {
            return PostRequestBuilder(context, url)
        }

        @JvmStatic
        fun put(context: Context, url: String): PostRequestBuilder {
            return PostRequestBuilder(context, url, HttpMethod.PUT)
        }

        @JvmStatic
        fun delete(context: Context, url: String): PostRequestBuilder {
            return PostRequestBuilder(context, url, HttpMethod.DELETE)
        }

        @JvmStatic
        fun patch(context: Context, url: String): PostRequestBuilder {
            return PostRequestBuilder(context, url, HttpMethod.PATCH)
        }

        @JvmStatic
        fun method(context: Context, method: HttpMethod, url: String): PostRequestBuilder {
            return PostRequestBuilder(context, url, method)
        }

        @JvmStatic
        fun upload(context: Context, url: String): UploadRequestBuilder {
            return UploadRequestBuilder(context, url)
        }

        @JvmStatic
        fun upload(context: Context, method: HttpMethod, url: String): UploadRequestBuilder {
            return UploadRequestBuilder(context, url, method)
        }

        @JvmStatic
        fun cancel(tag: String) {
            val callList = DoRequestService.callMap[tag]

            if (null != callList) {
                for (call in callList) {
                    call.cancel()
                }
                DoRequestService.callMap.remove(tag)
            }
        }

        @JvmStatic
        fun cancelAll() {
            for ((_, callList) in DoRequestService.callMap) {
                for (call in callList) {
                    call.cancel()
                }
            }
            DoRequestService.callMap.clear()
        }

        @JvmStatic
        fun clearCookies(context: Context) {
            HttpCookiesRepository(context).clearCookies()
        }

        @JvmStatic
        fun intentEasyHttpRecord(context: Context) {
            val intent = Intent(context, EasyHttpRecordActivity::class.java)
            context.startActivity(intent)
        }
    }
}
