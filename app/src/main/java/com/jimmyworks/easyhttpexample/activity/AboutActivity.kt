package com.jimmyworks.easyhttpexample.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttpexample.R
import com.jimmyworks.easyhttpexample.databinding.ActivityAboutBinding
import com.jimmyworks.easyhttpexample.utils.UiUtils


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
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/af19git5/AndroidEasyHttp")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    UiUtils.toast(this, getString(R.string.open_github_error))
                }
            }
        }
    }
}
