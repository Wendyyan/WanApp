package com.zyf.wanapp.util

import android.content.Context
import android.util.TypedValue

/**
 * Created by zyf on 2018/9/4.
 */
object CommonUtil {

    fun dp2px(context: Context, dpValue: Float): Int =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
}