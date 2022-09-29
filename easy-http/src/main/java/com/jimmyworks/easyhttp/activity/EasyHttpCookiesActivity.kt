package com.jimmyworks.easyhttp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.activity.model.EasyHttpCookiesViewModel
import com.jimmyworks.easyhttp.adapter.EasyHttpCookieHostAdapter
import com.jimmyworks.easyhttp.databinding.EasyHttpActivityCookiesBinding


class EasyHttpCookiesActivity : SingleClickActivity() {

    private lateinit var binding: EasyHttpActivityCookiesBinding
    private lateinit var viewModel: EasyHttpCookiesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EasyHttpActivityCookiesBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(EasyHttpCookiesViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initRecyclerView()
    }

    override fun onSingleClick(view: View) {
        when (view.id) {
            binding.ivClose.id -> {
                finish()
            }
            binding.ivClear.id -> {
                AlertDialog.Builder(this, R.style.EasyHttpAlertDialogTheme)
                    .setTitle(R.string.easy_http_notice)
                    .setMessage(R.string.easy_http_cookies_clear_notice)
                    .setPositiveButton(R.string.easy_http_yes) { _, _ -> EasyHttp.clearCookies(this) }
                    .setNegativeButton(R.string.easy_http_no) { _, _ -> }
                    .show()
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvCookies.layoutManager = LinearLayoutManager(this)
        val adapter = EasyHttpCookieHostAdapter(this)
        binding.rvCookies.adapter = adapter

        viewModel.cookieHostList.observe(this) {
            adapter.submitList(it)
            viewModel.noRecord.set(it.isEmpty())
        }
    }
}
