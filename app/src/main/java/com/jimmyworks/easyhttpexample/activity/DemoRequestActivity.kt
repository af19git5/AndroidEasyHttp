package com.jimmyworks.easyhttpexample.activity

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.exception.HttpException
import com.jimmyworks.easyhttp.listener.StringResponseListener
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttpexample.R
import com.jimmyworks.easyhttpexample.activity.model.DemoRequestViewModel
import com.jimmyworks.easyhttpexample.adapter.HeadersAdapter
import com.jimmyworks.easyhttpexample.data.Header
import com.jimmyworks.easyhttpexample.databinding.ActivityDemoRequestBinding
import com.jimmyworks.easyhttpexample.databinding.AlertAddHeaderBinding
import com.jimmyworks.easyhttpexample.utils.UiUtils
import okhttp3.Headers


class DemoRequestActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDemoRequestBinding
    private lateinit var viewModel: DemoRequestViewModel

    private lateinit var headersAdapter: HeadersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoRequestBinding.inflate(layoutInflater)
        viewModel = DemoRequestViewModel(intent.getBooleanExtra("isUpload", false))
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initRecyclerView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.ivClose.id -> {
                finish()
            }
            binding.tvMethod.id -> {
                httpMethodMenu(view)
            }
            binding.ivAddHeaders.id -> {
                alertAddHeader()
            }
            binding.fabSend.id -> {
                doRequest()
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvHeaders.layoutManager = layoutManager
        headersAdapter = HeadersAdapter(this, viewModel.headerList)
        binding.rvHeaders.adapter = headersAdapter
    }

    private fun httpMethodMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener {
            viewModel.method.value = HttpMethod.toHttpMethod(it.title.toString())
            true
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_http_method, popup.menu)
        popup.show()
    }

    private fun alertAddHeader() {
        val addHeaderBinding = AlertAddHeaderBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle("Add Header")
            .setView(addHeaderBinding.root)
            .setPositiveButton(R.string.yes, null)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setCancelable(false)
            .create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(View.OnClickListener {
                    if (addHeaderBinding.etKey.text.isEmpty()) {
                        UiUtils.toast(this, R.string.key_empty_hint)
                        return@OnClickListener
                    }

                    if (addHeaderBinding.etValue.text.isEmpty()) {
                        UiUtils.toast(this, R.string.value_empty_hint)
                        return@OnClickListener
                    }

                    viewModel.headerList.removeIf {
                        it.key == addHeaderBinding.etKey.text.toString()
                    }

                    viewModel.headerList.add(
                        Header(
                            addHeaderBinding.etKey.text.toString(),
                            addHeaderBinding.etValue.text.toString()
                        )
                    )
                    headersAdapter.notifyItemInserted(viewModel.headerList.size - 1)
                    alertDialog.dismiss()
                })
        }
        alertDialog.show()
    }

    private fun doRequest() {
        val alertDialog = UiUtils.loadingAlertDialog(this)

        val headerMap: MutableMap<String, String> = HashMap()
        var contentType = "text/plain"

        for (header in viewModel.headerList) {
            headerMap[header.key] = header.value
            if (header.key.equals("Content-Type", true)) {
                contentType = header.value
            }
        }

        if (viewModel.isUpload.value == true) {
            alertDialog.dismiss()
        } else {
            EasyHttp.method(this, viewModel.method.value!!, viewModel.url)
                .headers(headerMap)
                .stringBody(contentType, viewModel.body)
                .build()
                .getAsString(object : StringResponseListener {
                    override fun onSuccess(headers: Headers, body: String) {
                        UiUtils.toast(this@DemoRequestActivity, R.string.success)
                        viewModel.response.value = body
                        alertDialog.dismiss()
                    }

                    override fun onError(e: HttpException) {
                        UiUtils.toast(this@DemoRequestActivity, R.string.error)
                        alertDialog.dismiss()
                    }
                })
        }

    }
}
