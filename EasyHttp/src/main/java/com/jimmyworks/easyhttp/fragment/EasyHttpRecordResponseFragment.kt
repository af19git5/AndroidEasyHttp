package com.jimmyworks.easyhttp.fragment

import android.graphics.BitmapFactory
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
import com.jimmyworks.easyhttp.databinding.EasyHttpFragmentRecordResponseBinding
import com.jimmyworks.easyhttp.fragment.model.EasyHttpRecordResponseViewModel
import com.jimmyworks.easyhttp.utils.CommonUtils
import com.jimmyworks.easyhttp.utils.CommonUtils.Companion.prettify
import com.jimmyworks.easyhttp.utils.FileUtils.Companion.readText
import okhttp3.MediaType.Companion.toMediaType
import java.io.File


class EasyHttpRecordResponseFragment : Fragment() {

    private lateinit var binding: EasyHttpFragmentRecordResponseBinding
    private lateinit var viewModel: EasyHttpRecordResponseViewModel
    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EasyHttpFragmentRecordResponseBinding.inflate(inflater)
        val bundle = arguments
        if (null != bundle) {
            recordId = bundle.getLong("recordId", 0)
        }
        viewModel =
            ViewModelProvider(requireActivity()).get(EasyHttpRecordResponseViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initData()
        return binding.root
    }

    private fun initData() {
        val httpRecordRepository = HttpRecordRepository(requireContext())
        val responseDir: File =
            CommonUtils.getDiskCacheDir(requireContext(), EasyHttpConfig.RESPONSE_DIR_NAME)
        httpRecordRepository
            .findResponseHeadersByRecordId(recordId)
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

                viewModel.showImage.value = false
                viewModel.imageBody.value = null

                if (httpRecordInfo.responseContentType == null) {
                    val body = File(responseDir, httpRecordInfo.id.toString()).readText()
                    viewModel.body.value = body.prettify(httpRecordInfo.responseContentType)
                    viewModel.haveBody.value = body.isNotEmpty()
                } else {
                    val mediaType = httpRecordInfo.responseContentType!!.toMediaType()

                    if (mediaType.type.equals("image", true)) {
                        viewModel.showImage.value = true
                        viewModel.haveBody.value = true
                        viewModel.imageBody.value = BitmapFactory.decodeFile(
                            File(
                                responseDir,
                                httpRecordInfo.id.toString()
                            ).path
                        )
                    } else {
                        val body = File(responseDir, httpRecordInfo.id.toString()).readText()
                        viewModel.body.value = body.prettify(mediaType)
                        viewModel.haveBody.value = body.isNotEmpty()
                    }
                }
            }
    }

}
