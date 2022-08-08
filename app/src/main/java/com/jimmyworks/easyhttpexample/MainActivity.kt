package com.jimmyworks.easyhttpexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.exception.HttpException
import com.jimmyworks.easyhttp.listener.StringResponseListener
import com.jimmyworks.easyhttpexample.databinding.ActivityMainBinding
import okhttp3.Headers

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

                    }

                    override fun onError(e: HttpException) {

                    }
                })
        }

        binding.btIntent.setOnClickListener {
            EasyHttp.intentEasyHttpRecord(this)
        }
    }

}
