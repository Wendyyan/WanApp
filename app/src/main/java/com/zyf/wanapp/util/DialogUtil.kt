package com.zyf.wanapp.util

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.zyf.wanapp.R

/**
 * Created by zyf on 2018/9/1.
 */
object DialogUtil {

    /**
     * 获取一个耗时的对话框 ProgressDialog
     *
     * @param context
     * @param message
     * @return
     */
    fun getWaitDialog(context: Context, message: String): ProgressDialog {
        val waitDialog = ProgressDialog(context)
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message)
        }
        return waitDialog
    }

    fun getConfirmDialog(context: Context, message: String,
                         onClickListener: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = getDialog(context)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.info_confirm), onClickListener)
        builder.setNegativeButton(context.getString(R.string.info_cancel), null)
        return builder
    }

    /**
     * 获取一个Dialog
     *
     * @param context
     * @return
     */
    private fun getDialog(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }
}