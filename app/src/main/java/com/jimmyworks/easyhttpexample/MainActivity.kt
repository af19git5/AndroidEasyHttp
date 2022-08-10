package com.jimmyworks.easyhttpexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttpexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
