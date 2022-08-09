package com.jimmyworks.easyhttp.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.databinding.EasyHttpActivityRecordDetailBinding
import com.jimmyworks.easyhttp.fragment.EasyHttpRecordInfoFragment
import com.jimmyworks.easyhttp.fragment.EasyHttpRecordRequestFragment
import com.jimmyworks.easyhttp.fragment.EasyHttpRecordResponseFragment


class EasyHttpRecordDetailActivity : SingleClickActivity() {

    private lateinit var binding: EasyHttpActivityRecordDetailBinding
    private var recordId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EasyHttpActivityRecordDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        recordId = intent.getLongExtra("recordId", 0)
        initViewPager()
    }

    override fun onSingleClick(view: View) {
        when (view.id) {
            binding.ivClose.id -> {
                finish()
            }
        }
    }

    private fun initViewPager() {
        val titles = resources.getStringArray(R.array.easy_http_record_details_title)
        val fragments = arrayOf(
            EasyHttpRecordInfoFragment(),
            EasyHttpRecordRequestFragment(),
            EasyHttpRecordResponseFragment(),
        )

        binding.vpDetail.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                val fragment = fragments[position]
                val bundle = Bundle()
                bundle.putLong("recordId", recordId)
                fragment.arguments = bundle
                return fragment
            }

            override fun getItemCount(): Int {
                return fragments.size
            }
        }
        binding.vpDetail.offscreenPageLimit = 3
        TabLayoutMediator(binding.tlTabs, binding.vpDetail) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }
}
