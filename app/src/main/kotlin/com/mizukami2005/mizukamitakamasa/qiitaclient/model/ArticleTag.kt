package com.mizukami2005.mizukamitakamasa.qiitaclient.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by mizukamitakamasa on 2016/11/19.
 */
data class ArticleTag(val id: String,
                      val icon_url: String,
                      val followers_count: Int) : Parcelable {

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<ArticleTag> = object : Parcelable.Creator<ArticleTag> {
      override fun createFromParcel(source: Parcel): ArticleTag = source.run {
        ArticleTag(readString(), readString(), readInt())
      }

      override fun newArray(size: Int): Array<ArticleTag?> = kotlin.arrayOfNulls(size)
    }
  }

  override fun describeContents(): Int = 0

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.run {
      writeString(id)
      writeString(icon_url)
      writeInt(followers_count)
    }
  }
}