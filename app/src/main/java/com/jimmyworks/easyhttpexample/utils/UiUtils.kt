package com.jimmyworks.easyhttpexample.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jimmyworks.easyhttpexample.R
import com.jimmyworks.easyhttpexample.databinding.AlertLoadingBinding

class UiUtils {

    companion object {

        fun toast(context: Context, string: String) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
        }

        fun toast(context: Context, @StringRes strRes: Int) {
            Toast.makeText(context, strRes, Toast.LENGTH_SHORT).show()
        }

        fun loadingAlertDialog(activity: AppCompatActivity): AlertDialog {
            return AlertDialog.Builder(activity, R.style.AlertDialogTheme)
                .setView(AlertLoadingBinding.inflate(activity.layoutInflater).root)
                .setCancelable(false)
                .show()
        }
    }
}
