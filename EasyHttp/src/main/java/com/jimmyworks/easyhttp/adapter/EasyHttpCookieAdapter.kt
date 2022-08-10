package com.jimmyworks.easyhttp.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jimmyworks.easyhttp.EasyHttp
import com.jimmyworks.easyhttp.R
import com.jimmyworks.easyhttp.adapter.model.EasyHttpCookieItemViewModel
import com.jimmyworks.easyhttp.database.entity.HttpCookies
import com.jimmyworks.easyhttp.databinding.EasyHttpRvItemCookieBinding
import com.jimmyworks.easyhttp.utils.CommonUtils.Companion.setOnSingleClick
import com.jimmyworks.easyhttp.utils.DateUtils.Companion.toString


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
        itemBinding.ivClear.setOnSingleClick {
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
        itemBinding.root.setOnSingleClick {
            val builder = SpannableStringBuilder()
                .append("Host: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.host)
                .append("\n")
                .append("Value: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.cookieValue)
                .append("\n")
                .append("Expires: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.expires.toString("yyyy/MM/dd HH:mm:ss"))
                .append("\n")
                .append("Domain: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.domain)
                .append("\n")
                .append("Path: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.path)
                .append("\n")
                .append("Secure: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.secure.toString())
                .append("\n")
                .append("HttpOnly: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append(data.httpOnly.toString())

            val alertDialog = AlertDialog.Builder(context, R.style.EasyHttpAlertDialogTheme)
                .setTitle(data.cookieName)
                .setMessage(builder)
                .setPositiveButton(R.string.easy_http_yes) { _, _ -> }
                .show()

            val dialogTextView: TextView = alertDialog.findViewById(android.R.id.message)!!
            dialogTextView.setLineSpacing(0f, 1.2f)
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
