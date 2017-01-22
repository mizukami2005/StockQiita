package com.mizukami2005.mizukamitakamasa.qiitaclient.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by mizukamitakamasa on 2016/12/31.
 */
open class RealmArticle(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    open var id: String? = null,

    @SerializedName("title")
    @Expose
    open var title: String? = null,

    @SerializedName("url")
    @Expose
    open var url: String? = null,

    @SerializedName("body")
    @Expose
    open var body: String? = null,

    @SerializedName("created_at")
    @Expose
    open var createdAt: String? = null,

    open var type: String? = null,

    @SerializedName("user")
    @Expose
    open var user: RealmUser? = null
) : RealmObject() {}