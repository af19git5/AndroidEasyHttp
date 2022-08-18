package com.jimmyworks.easyhttpexample.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Patterns
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.exception.HttpException
import com.jimmyworks.easyhttp.listener.StringResponseListener
import com.jimmyworks.easyhttp.service.DoRequestService
import com.jimmyworks.easyhttp.type.HttpMethod
import com.jimmyworks.easyhttpexample.R
import com.jimmyworks.easyhttpexample.activity.model.DemoRequestViewModel
import com.jimmyworks.easyhttpexample.adapter.FormAdapter
import com.jimmyworks.easyhttpexample.adapter.HeadersAdapter
import com.jimmyworks.easyhttpexample.data.Form
import com.jimmyworks.easyhttpexample.data.Header
import com.jimmyworks.easyhttpexample.databinding.ActivityDemoRequestBinding
import com.jimmyworks.easyhttpexample.databinding.AlertAddFormBinding
import com.jimmyworks.easyhttpexample.databinding.AlertAddHeaderBinding
import com.jimmyworks.easyhttpexample.utils.UiUtils
import okhttp3.Headers


class DemoRequestActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDemoRequestBinding
    private lateinit var viewModel: DemoRequestViewModel

    private lateinit var headersAdapter: HeadersAdapter
    private lateinit var formAdapter: FormAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var addFormBinding: AlertAddFormBinding? = null
    private var chooseFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoRequestBinding.inflate(layoutInflater)
        viewModel = DemoRequestViewModel(intent.getBooleanExtra("isUpload", false))
        viewModel.title.value =
            if (viewModel.isUpload.value == true)
                getString(R.string.demo_http_upload)
            else getString(
                R.string.demo_http_request
            )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val uri = data.data ?: return@registerForActivityResult
                    val addFormBinding = this.addFormBinding ?: return@registerForActivityResult
                    var fileName = ""
                    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        fileName = cursor.getString(nameIndex)
                    }
                    chooseFileUri = uri
                    addFormBinding.etValue.setText(fileName)
                }
            }
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
            binding.ivAddForm.id -> {
                alertAddForm()
            }
            binding.fabSend.id -> {
                doRequest()
            }
        }
    }

    private fun initRecyclerView() {
        val headerLayoutManager = FlexboxLayoutManager(this)
        headerLayoutManager.flexDirection = FlexDirection.ROW
        headerLayoutManager.flexWrap = FlexWrap.WRAP
        headerLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvHeaders.layoutManager = headerLayoutManager
        headersAdapter = HeadersAdapter(this, viewModel.headerList)
        binding.rvHeaders.adapter = headersAdapter

        val formLayoutManager = FlexboxLayoutManager(this)
        formLayoutManager.flexDirection = FlexDirection.ROW
        formLayoutManager.flexWrap = FlexWrap.WRAP
        formLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvForm.layoutManager = formLayoutManager
        formAdapter = FormAdapter(this, viewModel.formList)
        binding.rvForm.adapter = formAdapter
    }

    private fun httpMethodMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener {
            viewModel.method.value = HttpMethod.toHttpMethod(it.title.toString())
            true
        }
        val inflater: MenuInflater = popup.menuInflater
        if (viewModel.isUpload.value == true) {
            inflater.inflate(R.menu.menu_http_upload_method, popup.menu)
        } else {
            inflater.inflate(R.menu.menu_http_method, popup.menu)
        }
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

    private fun alertAddForm() {
        addFormBinding = AlertAddFormBinding.inflate(layoutInflater)
        val addFormBinding = this.addFormBinding!!
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle("Add Form")
            .setView(addFormBinding.root)
            .setPositiveButton(R.string.yes, null)
            .setNegativeButton(R.string.no) { _, _ -> }
            .setCancelable(false)
            .create()
        addFormBinding.tbType.setOnCheckedChangeListener { _, isCheck ->
            addFormBinding.etValue.setText("")
            if (isCheck) {
                addFormBinding.etValue.isFocusable = false
                addFormBinding.etValue.isFocusableInTouchMode = false
            } else {
                addFormBinding.etValue.isFocusable = true
                addFormBinding.etValue.isFocusableInTouchMode = true
            }
        }

        addFormBinding.etValue.setOnClickListener {
            if (addFormBinding.tbType.isChecked) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                resultLauncher.launch(intent)
            }
        }

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(View.OnClickListener {
                    if (addFormBinding.etKey.text.isEmpty()) {
                        UiUtils.toast(this, R.string.key_empty_hint)
                        return@OnClickListener
                    }

                    if (addFormBinding.etValue.text.isEmpty()) {
                        UiUtils.toast(this, R.string.value_empty_hint)
                        return@OnClickListener
                    }

                    viewModel.formList.removeIf {
                        it.key == addFormBinding.etKey.text.toString()
                    }

                    if (addFormBinding.tbType.isChecked) {
                        viewModel.formList.add(
                            Form(
                                addFormBinding.etKey.text.toString(),
                                addFormBinding.tbType.isChecked,
                                chooseFileUri!!
                            )
                        )
                    } else {
                        viewModel.formList.add(
                            Form(
                                addFormBinding.etKey.text.toString(),
                                addFormBinding.tbType.isChecked,
                                addFormBinding.etValue.text.toString()
                            )
                        )
                    }
                    formAdapter.notifyItemInserted(viewModel.formList.size - 1)
                    this.addFormBinding = null
                    alertDialog.dismiss()
                })
        }
        alertDialog.show()
    }

    private fun doRequest() {

        if (!Patterns.WEB_URL.matcher(viewModel.url).matches()) {
            UiUtils.toast(this, getString(R.string.url_error_hint))
            return
        }

        val alertDialog = UiUtils.loadingAlertDialog(this)

        val headerMap: MutableMap<String, String> = HashMap()
        var contentType =
            if (viewModel.isUpload.value == true) "multipart/form-data" else "text/plain"

        for (header in viewModel.headerList) {
            headerMap[header.key] = header.value
            if (header.key.equals("Content-Type", true)) {
                contentType = header.value
            }
        }

        val doRequest: DoRequestService

        if (viewModel.isUpload.value == true) {
            val builder = EasyHttp.upload(this, viewModel.method.value!!, viewModel.url)
                .headers(headerMap)
                .contentType(contentType)

            for (form in viewModel.formList) {
                if (form.isFile) {
                    builder.addMultipartFile(form.key, form.value as Uri)
                } else {
                    builder.addMultipartParam(form.key, form.value as String)
                }
            }
            doRequest = builder.build()
        } else {
            doRequest = EasyHttp.method(this, viewModel.method.value!!, viewModel.url)
                .headers(headerMap)
                .stringBody(contentType, viewModel.body)
                .build()
        }

        doRequest.getAsString(object : StringResponseListener {
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
