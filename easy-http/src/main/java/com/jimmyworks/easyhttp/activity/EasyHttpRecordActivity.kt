package com.jimmyworks.easyhttp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.activity.model.EasyHttpRecordViewModel
import com.jimmyworks.easyhttp.adapter.EasyHttpRecordAdapter
import com.jimmyworks.easyhttp.databinding.EasyHttpActivityRecordBinding


class EasyHttpRecordActivity : SingleClickActivity() {

    private lateinit var binding: EasyHttpActivityRecordBinding
    private lateinit var viewModel: EasyHttpRecordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EasyHttpActivityRecordBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(EasyHttpRecordViewModel::class.java)
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
                    .setMessage(R.string.easy_http_record_clear_notice)
                    .setPositiveButton(R.string.easy_http_yes) { _, _ -> viewModel.clearRecord() }
                    .setNegativeButton(R.string.easy_http_no) { _, _ -> }
                    .show()
            }
            binding.ivClearSearch.id -> {
                viewModel.searchText.value = ""
            }
            binding.fabCookie.id -> {
                EasyHttp.intentEasyHttpCookies(this)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvRecord.layoutManager = LinearLayoutManager(this)
        val adapter = EasyHttpRecordAdapter(this)
        binding.rvRecord.adapter = adapter
        viewModel.recordList.observe(this) {
            viewModel.noRecord.set(it.isEmpty())
            adapter.submitList(it)
        }
    }
}
