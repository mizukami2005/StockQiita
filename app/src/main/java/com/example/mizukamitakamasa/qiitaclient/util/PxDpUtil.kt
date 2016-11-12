package com.example.mizukamitakamasa.qiitaclient.util

import android.content.Context

/**
 * Created by mizukamitakamasa on 2016/11/06.
 */
class PxDpUtil {
  fun dpToPx(context: Context, dip: Int): Float {
    val scale = context.resources.displayMetrics.density
    return (dip * scale + 0.5f)
  }
}