package com.zyf.wanapp.util

import android.content.Context
import com.zyf.wanapp.App
import com.zyf.wanapp.constant.Constant
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by zyf on 2018/8/30.
 */

object DelegateExt{

    fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

    var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    var username: String by Preference(Constant.USERNAME_KEY, "")

    var pwd: String by Preference(Constant.PASSWORD_KEY, "")

    var token: String by Preference(Constant.TOKEN, "")

    var isNightMode: Boolean by Preference(Constant.IS_NIGHT_MODE, false)

    var switchNoPhotoMode: Boolean by Preference(Constant.SWITCH_NO_PHOTO_MODE, false)

}

private class NotNullSingleValueVar<T>: ReadWriteProperty<Any?, T> {

    private var value: T? =null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}

class Preference<T>(val name: String, private val default: T):
        ReadWriteProperty<Any?, T> {

    companion object {

        private val prefs by lazy {
            App.instance.getSharedPreferences("default", Context.MODE_PRIVATE)
        }

        /**
         * 删除全部数据
         */
        fun clearPreference() {
            prefs.edit().clear().apply()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T = with(prefs){
        val res: Any = when(default){
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        res as T
    }

    private fun <T> putPreference(name: String, value: T) = with(prefs.edit()){
        when(value){
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int ->putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }.apply()
    }
}
