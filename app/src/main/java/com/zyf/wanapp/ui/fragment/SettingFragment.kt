package com.zyf.wanapp.ui.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.zyf.wanapp.BuildConfig
import com.zyf.wanapp.R
import com.zyf.wanapp.ui.activity.SettingActivity
import com.zyf.wanapp.util.CacheDataUtil
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast

/**
 * Created by zyf on 2018/9/7.
 */
class SettingFragment : PreferenceFragmentCompat() {

    private var context: SettingActivity? = null

    companion object {
        fun getInstance() = SettingFragment()
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.pref_settings)

        context = activity as SettingActivity
        setDefaultText()

        findPreference<Preference>("clear_cache")!!.onPreferenceClickListener =
                androidx.preference.Preference.OnPreferenceClickListener {
                    CacheDataUtil.clearAllCache(context!!)
                    context!!.toast(getString(R.string.info_clear_cache_successfully))
                    setDefaultText()
                    false
                }

        try {
            val version = getString(R.string.info_current_version) + BuildConfig.VERSION_NAME
            findPreference<Preference>("version")!!.summary = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        findPreference<Preference>("source_code")!!.onPreferenceClickListener =
                androidx.preference.Preference.OnPreferenceClickListener {
                    context?.browse(getString(R.string.info_source_code_url))
                    false
                }
    }

    private fun setDefaultText() {
        try {
            findPreference<Preference>("clear_cache")!!.summary = CacheDataUtil.getTotalCacheSize(context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}