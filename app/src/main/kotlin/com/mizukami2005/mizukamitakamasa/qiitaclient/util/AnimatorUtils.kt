package com.mizukami2005.mizukamitakamasa.qiitaclient.util

import android.animation.ObjectAnimator
import android.view.View

/**
 * Created by mizukamitakamasa on 2016/11/06.
 */
class AnimatorUtils {
  val ALPHA = "alpha"
  val ROTATION = "rotation"
  val TRANSLATION_Y = "translationY"

  fun translationY(view: View, value: Float) {
    val animate = ObjectAnimator.ofFloat(view, TRANSLATION_Y, value)
  }

  fun alpha(view: View, beforeValue: Float, afterValue: Float) {
    val animate = ObjectAnimator.ofFloat(view, ALPHA, beforeValue, afterValue)
  }

  fun rotation(view: View, value: Float) {
    val animate = ObjectAnimator.ofFloat(view, ROTATION, value)
  }

  fun fabOpen(view: View, iconWhile: Float) {
  }

  fun fabClose() {
  }
}