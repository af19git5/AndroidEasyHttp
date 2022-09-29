package com.jimmyworks.easyhttp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jimmyworks.easyhttp.activity.EasyHttpRecordDetailActivity
import com.jimmyworks.easyhttp.adapter.model.EasyHttpRecordItemViewModel
import com.jimmyworks.easyhttp.database.entity.HttpRecordInfo
import com.jimmyworks.easyhttp.databinding.EasyHttpRvItemRecordBinding
import com.jimmyworks.easyhttp.utils.CommonUtils.Companion.setOnSingleClick


class EasyHttpRecordAdapter(private val context: Context) :
    ListAdapter<HttpRecordInfo, RecyclerView.ViewHolder>(recordDiff) {

    class ItemViewHolder(val itemBinding: EasyHttpRvItemRecordBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            EasyHttpRvItemRecordBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemBinding = (viewHolder as ItemViewHolder).itemBinding
        val httpRecordInfo = getItem(position)
        itemBinding.itemViewModel = EasyHttpRecordItemViewModel(httpRecordInfo)
        itemBinding.clRecord.setOnSingleClick {
            val intent = Intent(it.context, EasyHttpRecordDetailActivity::class.java)
            intent.putExtra("recordId", httpRecordInfo.id)
            context.startActivity(intent)
        }
    }

    companion object {
        private val recordDiff = object : DiffUtil.ItemCallback<HttpRecordInfo>() {
            override fun areItemsTheSame(
                oldItem: HttpRecordInfo,
                newItem: HttpRecordInfo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HttpRecordInfo,
                newItem: HttpRecordInfo
            ): Boolean {
                return oldItem.url == newItem.url
                        && oldItem.method == newItem.method
                        && oldItem.requestContentType == newItem.requestContentType
                        && oldItem.responseContentType == newItem.responseContentType
                        && oldItem.statusCode == newItem.statusCode
                        && oldItem.errorMessage == newItem.errorMessage
                        && oldItem.sendTime == newItem.sendTime
                        && oldItem.receiveTime == newItem.receiveTime
            }
        }
    }
}
