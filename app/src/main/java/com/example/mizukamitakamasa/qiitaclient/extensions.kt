package com.example.mizukamitakamasa.qiitaclient

import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.widget.Toast

/**
 * Created by mizukamitakamasa on 2016/09/25.
 */

fun <T : View> View.bindView(@IdRes id: Int): Lazy<T> = lazy {
  findViewById(id) as T
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
  Toast.makeText(this, message, duration).show()
}