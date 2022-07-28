package com.sfs.artery.certification.app.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface SharePreferenceInterface {
    fun setString(key: String, default: String)
    fun getString(key: String, default: String): String
    fun setBoolean(key: String, default: Boolean)
    fun getBoolean(key: String, default: Boolean): Boolean
}

class SharedPrefereces
@Inject constructor(@ApplicationContext private val context: Context) : SharePreferenceInterface {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    override fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    override fun getString(key: String, default: String): String {
        return prefs.getString(key, default).toString()
    }

    override fun setBoolean(key: String, default: Boolean) {
        prefs.edit().putBoolean(key, default).apply()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return prefs.getBoolean(key, false)
    }
}