package com.jimmyworks.easyhttpexample.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttpexample.databinding.ActivityAboutBinding


class AboutActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.ivClose.id -> {
                finish()
            }
            binding.ivGithub.id -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://github.com/af19git5/AndroidEasyHttp")
                startActivity(intent)
            }
        }
    }
}
