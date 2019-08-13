package com.example.movieapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.movieapplication.R
import java.util.*

class LanguageManager {

    companion object {
        @JvmStatic
        fun setLocale(context: Context): Context {
            return updateResource(context, getLanguagePref(context))
        }

        @JvmStatic
        fun setNewLocale(context: Context, language: String): Context {
            setLanguagePref(context, language)
            return updateResource(context, language)
        }

        @JvmStatic
        fun getLanguagePref(context: Context): String {
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val temp: String? = sharedPreferences.getString(Const.LANGUAGE_KEY, Const.LANGUAGE_KEY_EN)
            if (temp != null) {
                return temp
            }
            return Const.LANGUAGE_KEY_EN
        }

        @JvmStatic
        fun setLanguagePref(context: Context, language: String) {
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            sharedPreferences.edit().putString(Const.LANGUAGE_KEY, language).apply()
        }

        @JvmStatic
        fun updateResource(context: Context, language: String): Context {
            val locale = Locale(language)
            val resource = context.resources
            val config = resource.configuration

            config.locale = locale
            resource.updateConfiguration(config, resource.displayMetrics)

            return context
        }

        @JvmStatic
        fun getLanguage(context: Context, code: String): String {
            when (code) {
                Const.LANGUAGE_KEY_EN -> return context.resources.getString(R.string.lang_en)
                Const.LANGUAGE_KEY_ES -> return context.resources.getString(R.string.lang_es)
                Const.LANGUAGE_KEY_FR -> return context.resources.getString(R.string.lang_fr)
                Const.LANGUAGE_KEY_JA -> return context.resources.getString(R.string.lang_ja)
            }
            return code
        }

        @JvmStatic
        fun getLanguage(context: Context): String {
            var code: String = getLanguagePref(context)
            when (code) {
                Const.LANGUAGE_KEY_EN -> return Const.URL_LANGUAGE_KEY_EN
                Const.LANGUAGE_KEY_ID -> return Const.URL_LANGUAGE_KEY_ID
            }
            return code
        }
    }
}