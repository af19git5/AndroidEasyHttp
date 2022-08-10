package com.jimmyworks.easyhttp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.adapter.model.EasyHttpCookieHostItemViewModel
import com.jimmyworks.easyhttp.database.dto.CookieHostDTO
import com.jimmyworks.easyhttp.database.repository.HttpCookiesRepository
import com.jimmyworks.easyhttp.databinding.EasyHttpRvItemCookieHostBinding
import com.jimmyworks.easyhttp.utils.CommonUtils.Companion.setOnSingleClick


class EasyHttpCookieHostAdapter(private val activity: AppCompatActivity) :
    ListAdapter<CookieHostDTO, RecyclerView.ViewHolder>(recordDiff) {

    class ItemViewHolder(val itemBinding: EasyHttpRvItemCookieHostBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            EasyHttpRvItemCookieHostBinding.inflate(
                LayoutInflater.from(activity),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemBinding = (viewHolder as ItemViewHolder).itemBinding
        val data = getItem(position)
        itemBinding.itemViewModel = EasyHttpCookieHostItemViewModel(data)
        itemBinding.ivClear.setOnSingleClick {
            AlertDialog.Builder(activity, R.style.EasyHttpAlertDialogTheme)
                .setTitle(R.string.easy_http_notice)
                .setMessage(
                    activity.getString(R.string.easy_http_cookies_host_clear_notice)
                            + " " + data.host + "."
                )
                .setPositiveButton(R.string.easy_http_yes) { _, _ ->
                    EasyHttp.clearCookies(activity, data.host)
                }
                .setNegativeButton(R.string.easy_http_no) { _, _ -> }
                .show()
        }

        val layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.FLEX_START
        itemBinding.rvCookies.layoutManager = layoutManager
        val adapter = EasyHttpCookieAdapter(activity)
        itemBinding.rvCookies.adapter = adapter
        HttpCookiesRepository(activity).findLiveByHost(data.host).observe(activity) {
            adapter.submitList(it)
        }
    }

    companion object {
        private val recordDiff = object : DiffUtil.ItemCallback<CookieHostDTO>() {
            override fun areItemsTheSame(
                oldItem: CookieHostDTO,
                newItem: CookieHostDTO
            ): Boolean {
                return oldItem.host == newItem.host
            }

            override fun areContentsTheSame(
                oldItem: CookieHostDTO,
                newItem: CookieHostDTO
            ): Boolean {
                return oldItem.cookieCount == newItem.cookieCount
            }
        }
    }
}
