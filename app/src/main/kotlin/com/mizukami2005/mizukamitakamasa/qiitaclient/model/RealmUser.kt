package com.mizukami2005.mizukamitakamasa.qiitaclient.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by mizukamitakamasa on 2016/12/31.
 */
open class RealmUser(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    open var id: String? = null,

    @SerializedName("name")
    @Expose
    open var name: String? = null,

    @SerializedName("profile_image_url")
    @Expose
    open var profileImageUrl: String? = null
) : RealmObject() {}