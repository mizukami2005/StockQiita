package com.mizukami2005.mizukamitakamasa.qiitaclient.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by mizukamitakamasa on 2016/09/25.
 */

data class Article(var id: String = "",
                   var title: String = "",
                   var url: String = "",
                   var body: String = "",
                   var createdAt: String = "",
                   var type: String = "",
                   var user: User = User()) : Parcelable {

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<Article> = object : Parcelable.Creator<Article> {
      override fun createFromParcel(source: Parcel): Article = source.run {
        Article(readString(), readString(), readString(), readString(), readString(), readString(), readParcelable(Article::class.java.classLoader))
      }

      override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
    }
  }

  override fun describeContents(): Int = 0

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.run {
      writeString(id)
      writeString(title)
      writeString(url)
      writeString(body)
      writeString(createdAt)
      writeString(type)
      writeParcelable(user, flags)
    }
  }
}