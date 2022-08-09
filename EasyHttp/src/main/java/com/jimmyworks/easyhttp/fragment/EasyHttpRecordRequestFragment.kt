package com.jimmyworks.easyhttp.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jimmyworks.easyhttp.EasyHttpConfig
import com.jimmyworks.easyhttp.database.repository.HttpRecordRepository
import com.jimmyworks.easyhttp.databinding.EasyHttpFragmentRecordRequestBinding
import com.jimmyworks.easyhttp.fragment.model.EasyHttpRecordRequestViewModel
import com.jimmyworks.easyhttp.utils.CommonUtils
import com.jimmyworks.easyhttp.utils.CommonUtils.Companion.prettify
import com.jimmyworks.easyhttp.utils.FileUtils.Companion.readText
import java.io.File


class EasyHttpRecordRequestFragment : Fragment() {

    private lateinit var binding: EasyHttpFragmentRecordRequestBinding
    private lateinit var viewModel: EasyHttpRecordRequestViewModel
    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EasyHttpFragmentRecordRequestBinding.inflate(inflater)
        val bundle = arguments
        if (null != bundle) {
            recordId = bundle.getLong("recordId", 0)
        }
        viewModel =
            ViewModelProvider(requireActivity()).get(EasyHttpRecordRequestViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initData()
        return binding.root
    }

    private fun initData() {
        val httpRecordRepository = HttpRecordRepository(requireContext())
        val requestDir: File =
            CommonUtils.getDiskCacheDir(requireContext(), EasyHttpConfig.REQUEST_DIR_NAME)

        httpRecordRepository
            .findRequestHeadersByRecordId(recordId)
            .observe(viewLifecycleOwner) { headerList ->

                val builder = SpannableStringBuilder()

                for ((i, header) in headerList.withIndex()) {
                    builder.append(
                        header.header + ": ",
                        StyleSpan(Typeface.BOLD),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    ).append(header.headerValue)
                    if (i != headerList.size - 1) {
                        builder.append("\n")
                    }
                }

                viewModel.headers.value = builder
                viewModel.haveHeaders.value = headerList.isNotEmpty()
            }
        httpRecordRepository
            .findById(recordId)
            .observe(viewLifecycleOwner) { httpRecordInfo ->
                val body = File(requestDir, httpRecordInfo.id.toString()).readText()
                viewModel.body.value = body.prettify(httpRecordInfo.requestContentType)
                viewModel.haveBody.value = body.isNotEmpty()
            }
    }
}
