package com.jimmyworks.easyhttp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jimmyworks.easyhttp.database.repository.HttpRecordRepository
import com.jimmyworks.easyhttp.databinding.EasyHttpFragmentRecordInfoBinding
import com.jimmyworks.easyhttp.fragment.model.EasyHttpRecordInfoViewModel


class EasyHttpRecordInfoFragment : Fragment() {

    private lateinit var binding: EasyHttpFragmentRecordInfoBinding
    private lateinit var viewModel: EasyHttpRecordInfoViewModel
    private var recordId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = EasyHttpFragmentRecordInfoBinding.inflate(inflater)
        val bundle = arguments
        if (null != bundle) {
            recordId = bundle.getLong("recordId", 0)
        }
        viewModel =
            ViewModelProvider(requireActivity()).get(EasyHttpRecordInfoViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initData()
        return binding.root
    }

    private fun initData() {
        HttpRecordRepository(requireContext())
            .findById(recordId)
            .observe(viewLifecycleOwner) { httpRecordInfo ->
                viewModel.setHttpRecordInfo(
                    httpRecordInfo
                )
            }
    }

}
