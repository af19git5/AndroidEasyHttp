package com.jimmyworks.easyhttpexample.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttpexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.llRequest.id -> {
                startActivity(Intent(this, DemoRequestActivity::class.java))
            }
            binding.llUpload.id -> {

            }
            binding.llRecord.id -> {
                EasyHttp.intentEasyHttpRecord(this)
            }
        }
    }
}
