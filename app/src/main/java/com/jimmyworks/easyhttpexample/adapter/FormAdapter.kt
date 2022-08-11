package com.jimmyworks.easyhttpexample.adapter

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.provider.OpenableColumns
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.jimmyworks.easyhttpexample.R
import com.jimmyworks.easyhttpexample.adapter.model.FormViewModel
import com.jimmyworks.easyhttpexample.data.Form
import com.jimmyworks.easyhttpexample.databinding.RvItemFormBinding


class FormAdapter(private val context: Context, private val formList: MutableList<Form>) :
    RecyclerView.Adapter<FormAdapter.ItemViewHolder>() {

    class ItemViewHolder(val itemBinding: RvItemFormBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun getItemCount(): Int {
        return formList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RvItemFormBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemBinding = holder.itemBinding
        val data = formList[position]

        val value: String = if (data.value is Uri) {
            var fileName = ""
            context.contentResolver.query(data.value, null, null, null, null)
                ?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    fileName = cursor.getString(nameIndex)
                }
            fileName
        } else {
            data.value as String
        }

        itemBinding.itemViewModel = FormViewModel(data.key, value)
        itemBinding.ivClear.setOnClickListener {
            AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(R.string.notice)
                .setMessage(
                    context.getString(R.string.form_clear_notice)
                            + " " + data.key + "."
                )
                .setPositiveButton(R.string.yes) { _, _ ->
                    formList.removeAt(position)
                    notifyItemRangeChanged(position, formList.size)
                    notifyItemRemoved(position)
                }
                .setNegativeButton(R.string.no) { _, _ -> }
                .show()
        }

        val builder = SpannableStringBuilder()
            .append("Type: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(if (data.isFile) context.getString(R.string.file) else context.getString(R.string.text))
            .append("\n")
            .append("Value: ", StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(value)

        itemBinding.root.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(data.key)
                .setMessage(builder)
                .setPositiveButton(R.string.yes) { _, _ -> }
                .show()
            val dialogTextView: TextView = alertDialog.findViewById(android.R.id.message)!!
            dialogTextView.setLineSpacing(0f, 1.2f)
        }


    }
}
