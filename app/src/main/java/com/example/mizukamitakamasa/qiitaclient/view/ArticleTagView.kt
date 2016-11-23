package com.example.mizukamitakamasa.qiitaclient.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mizukamitakamasa.qiitaclient.R
import com.example.mizukamitakamasa.qiitaclient.bindView
import com.example.mizukamitakamasa.qiitaclient.model.ArticleTag
import com.example.mizukamitakamasa.qiitaclient.util.TagUtils

/**
 * Created by mizukamitakamasa on 2016/11/19.
 */
class ArticleTagView: FrameLayout {

  constructor(context: Context?) : super(context)

  constructor(context: Context?,
              attrs: AttributeSet?) : super(context, attrs)

  constructor(context: Context?,
              attrs: AttributeSet?,
              defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  constructor(context: Context?,
              attrs: AttributeSet?,
              defStyleAttr: Int,
              defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  val tagImageView: ImageView by bindView(R.id.tag_image_view)

  val tagNameTextView: TextView by bindView(R.id.tag_name_text_view)

  val followersCountTextView: TextView by bindView(R.id.followers_count_text_view)

  val tagCheckBox: CheckBox by bindView(R.id.tag_check_box)

  init {
    LayoutInflater.from(context).inflate(R.layout.view_article_tag, this)
  }

  fun setArticleTag(articleTag: ArticleTag) {
    tagNameTextView.text = articleTag.id
    followersCountTextView.text = articleTag.followers_count.toString() + " Followers"
    Glide.with(context).load(articleTag.icon_url).into(tagImageView)
  }
}