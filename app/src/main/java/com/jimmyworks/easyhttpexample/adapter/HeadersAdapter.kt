package com.jimmyworks.easyhttpexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.jimmyworks.easyhttpexample.R
import com.jimmyworks.easyhttpexample.adapter.model.HeadersViewModel
import com.jimmyworks.easyhttpexample.data.Header
import com.jimmyworks.easyhttpexample.databinding.RvItemHeadersBinding


class HeadersAdapter(private val context: Context, private val headerList: MutableList<Header>) :
    RecyclerView.Adapter<HeadersAdapter.ItemViewHolder>() {

    class ItemViewHolder(val itemBinding: RvItemHeadersBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun getItemCount(): Int {
        return headerList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RvItemHeadersBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemBinding = holder.itemBinding
        val data = headerList[position]
        itemBinding.itemViewModel = HeadersViewModel(data)
        itemBinding.ivClear.setOnClickListener {
            AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(R.string.notice)
                .setMessage(
                    context.getString(R.string.header_clear_notice)
                            + " " + data.key + "."
                )
                .setPositiveButton(R.string.yes) { _, _ ->
                    headerList.removeAt(position)
                    notifyItemRangeChanged(position, headerList.size)
                    notifyItemRemoved(position)
                }
                .setNegativeButton(R.string.no) { _, _ -> }
                .show()
        }
        itemBinding.root.setOnClickListener {
            AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(data.key)
                .setMessage(data.value)
                .setPositiveButton(R.string.yes) { _, _ -> }
                .show()
        }
    }
}
