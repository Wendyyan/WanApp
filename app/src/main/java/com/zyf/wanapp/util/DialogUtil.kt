package com.zyf.wanapp.util

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils

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
}