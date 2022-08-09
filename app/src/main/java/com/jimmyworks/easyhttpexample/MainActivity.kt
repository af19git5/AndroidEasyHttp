package com.jimmyworks.easyhttpexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.exception.HttpException
import com.jimmyworks.easyhttp.listener.DownloadListener
import com.jimmyworks.easyhttp.listener.StringResponseListener
import com.jimmyworks.easyhttp.utils.CommonUtils
import com.jimmyworks.easyhttpexample.databinding.ActivityMainBinding
import okhttp3.Headers
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btGet.setOnClickListener {
            EasyHttp.get(
                this,
                "https://google.com"
            )
                .build()
                .getAsString(object : StringResponseListener {
                    override fun onSuccess(headers: Headers, body: String) {
                        Log.i("easyHttp", body)
                    }

                    override fun onError(e: HttpException) {
                        Log.e("easyHttp", e.message!!)
                    }
                })
        }

        binding.btPost.setOnClickListener {
            val map: MutableMap<String, String> = HashMap()
            map["text"] = "examplePost"
            EasyHttp.post(
                this,
                "http://192.168.59.125:8080/api/examplePost"
            )
                .addHeader("Token", "A12345678")
                .jsonBody(map)
                .build()
                .getAsString(object : StringResponseListener {
                    override fun onSuccess(headers: Headers, body: String) {
                        Log.i("easyHttp", body)
                    }

                    override fun onError(e: HttpException) {
                        Log.e("easyHttp", e.message!!)
                    }
                })
        }

        binding.btDownload.setOnClickListener {
            EasyHttp.get(
                this,
                "https://image.cache.storm.mg/styles/smg-800x533-fp/s3/media/image/2020/11/07/20201107-092915_U13380_M651499_4ac4.jpg?itok=6KFZde7p"
            ).build()
                .download(
                    File(CommonUtils.getDiskCacheDir(this, "testImage.jpg"), "test"),
                    object : DownloadListener {
                        override fun onSuccess(headers: Headers, file: File) {
                        }

                        override fun onProgress(downloadBytes: Long, totalBytes: Long) {
                        }

                        override fun onError(e: HttpException) {
                            Log.e("easyHttp", e.message!!)
                        }
                    })
        }

        val registerForActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (null == it.data || null == it.data!!.data) {
                return@registerForActivityResult
            }
            EasyHttp.upload(this, "http://192.168.59.125:8080/api/exampleUpload")
                .addHeader("Token", "A12345678")
                .addMultipartFile("file", it.data!!.data!!)
                .build()
                .getAsString(object : StringResponseListener {
                    override fun onSuccess(headers: Headers, body: String) {
                        Log.i("easyHttp", body)
                    }

                    override fun onError(e: HttpException) {
                        Log.e("easyHttp", e.message!!)
                    }
                })
        }

        binding.btUpload.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            registerForActivityResult.launch(Intent.createChooser(intent, "Select Picture"))
        }

        binding.btIntent.setOnClickListener {
            EasyHttp.intentEasyHttpRecord(this)
        }
    }


}
