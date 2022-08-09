package com.jimmyworks.easyhttp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.adapter.model.EasyHttpCookieItemViewModel
import com.jimmyworks.easyhttp.database.entity.HttpCookies
import com.jimmyworks.easyhttp.databinding.EasyHttpRvItemCookieBinding
import com.jimmyworks.easyhttp.utils.CommonUtils.Companion.onSingleClick


class EasyHttpCookieAdapter(private val context: Context) :
    ListAdapter<HttpCookies, RecyclerView.ViewHolder>(recordDiff) {

    class ItemViewHolder(val itemBinding: EasyHttpRvItemCookieBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            EasyHttpRvItemCookieBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemBinding = (viewHolder as ItemViewHolder).itemBinding
        val data = getItem(position)
        itemBinding.itemViewModel = EasyHttpCookieItemViewModel(data)
        itemBinding.ivClear.onSingleClick {
            AlertDialog.Builder(context, R.style.EasyHttpAlertDialogTheme)
                .setTitle(R.string.easy_http_notice)
                .setMessage(
                    context.getString(R.string.easy_http_cookies_name_clear_notice)
                            + " " + data.cookieName + "."
                )
                .setPositiveButton(R.string.easy_http_yes) { _, _ ->
                    EasyHttp.clearCookies(context, data.host, data.cookieName)
                }
                .setNegativeButton(R.string.easy_http_no) { _, _ -> }
                .show()
        }
    }

    companion object {
        private val recordDiff = object : DiffUtil.ItemCallback<HttpCookies>() {
            override fun areItemsTheSame(
                oldItem: HttpCookies,
                newItem: HttpCookies
            ): Boolean {
                return oldItem.host == newItem.host
                        && oldItem.cookieName == newItem.cookieName
            }

            override fun areContentsTheSame(
                oldItem: HttpCookies,
                newItem: HttpCookies
            ): Boolean {
                return oldItem.cookieValue == newItem.cookieValue
                        && oldItem.expires == newItem.expires
                        && oldItem.domain == newItem.domain
                        && oldItem.path == newItem.path
                        && oldItem.secure == newItem.secure
                        && oldItem.httpOnly == newItem.httpOnly
            }
        }
    }
}
