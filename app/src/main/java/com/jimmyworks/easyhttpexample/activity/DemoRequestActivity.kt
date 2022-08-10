package com.jimmyworks.easyhttpexample.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttpexample.databinding.ActivityDemoRequestBinding


class DemoRequestActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDemoRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoRequestBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.ivClose.id -> {
                finish()
            }
            binding.fabSend.id -> {

            }
        }
    }
}
