package com.jimmyworks.easyhttp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.activity.model.EasyHttpRecordViewModel
import com.jimmyworks.easyhttp.adapter.EasyHttpRecordAdapter
import com.jimmyworks.easyhttp.databinding.EasyHttpActivityRecordBinding
import com.jimmyworks.easyhttp.utils.CommonUtils


class EasyHttpRecordActivity : AppCompatActivity(), View.OnClickListener {

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

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(
            R.anim.easy_http_anim_slide_enter, R.anim.easy_http_anim_slide_exit
        )
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.easy_http_anim_back_enter, R.anim.easy_http_anim_back_exit
        )
    }

    override fun onClick(view: View) {
        if (CommonUtils.isFastDoubleClick(view, 1000)) {
            return
        }
        when (view.id) {
            binding.ivLeft.id -> {
                finish()
            }
            binding.ivRight.id -> {
                AlertDialog.Builder(this, R.style.EasyHttpAlertDialogTheme)
                    .setTitle(R.string.easy_http_notice)
                    .setMessage(R.string.easy_http_clear_notice)
                    .setPositiveButton(R.string.easy_http_yes) { _, _ -> viewModel.clearRecord() }
                    .setNegativeButton(R.string.easy_http_no) { _, _ -> }
                    .show()
            }
            binding.ivClear.id -> {
                viewModel.searchText.value = ""
            }
            binding.fabCookie.id -> {

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
