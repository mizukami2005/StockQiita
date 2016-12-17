package com.mizukami2005.mizukamitakamasa.qiitaclient

import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.widget.Toast

/**
 * Created by mizukamitakamasa on 2016/09/25.
 */

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
  Toast.makeText(this, message, duration).show()
}