package com.zyf.wanapp.util

import android.content.Context
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zyf on 2018/9/4.
 */

/**
 * dp转px
 */
fun dp2px(context: Context, dpValue: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.resources.displayMetrics).toInt()

/**
 * 格式化当前日期
 */
fun formatCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

/**
 * 关闭软键盘
 */
fun closeKeyBoard(editText: EditText, context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}