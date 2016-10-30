package com.example.mizukamitakamasa.qiitaclient.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by mizukamitakamasa on 2016/10/16.
 */
data class ResponseToken(val token: String) : Parcelable {

    companion object {
        @JvmField
        val CRETOR: Parcelable.Creator<ResponseToken> = object : Parcelable.Creator<ResponseToken> {
            override fun createFromParcel(source: Parcel): ResponseToken = source.run {
                ResponseToken(readString())
            }

            override fun newArray(size: Int): Array<ResponseToken?> = arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.run {
            writeString(token)
        }
    }
}