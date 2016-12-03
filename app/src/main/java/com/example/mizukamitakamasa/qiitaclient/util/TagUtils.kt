package com.example.mizukamitakamasa.qiitaclient.util

import android.content.Context

/**
 * Created by mizukamitakamasa on 2016/11/23.
 */
class TagUtils {

  val KEY = "TAG"

  fun saveName(context: Context, key: String, values: MutableSet<String>) {
    val pref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putStringSet(key, values)
    editor.apply()
  }

  fun loadName(context: Context, key: String): MutableSet<String> {
    val prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
    return prefs.getStringSet(key, mutableSetOf())
  }
}