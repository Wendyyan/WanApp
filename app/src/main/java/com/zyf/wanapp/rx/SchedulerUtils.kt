package com.zyf.wanapp.rx

/**
 * Created by zyf on 2018/8/31.
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> = IoMainScheduler()
}