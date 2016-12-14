package com.example.mizukamitakamasa.qiitaclient.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.mizukamitakamasa.qiitaclient.R
import com.example.mizukamitakamasa.qiitaclient.model.ArticleTag
import kotlinx.android.synthetic.main.view_article_tag.view.*

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

  init {
    LayoutInflater.from(context).inflate(R.layout.view_article_tag, this)
  }

  fun setArticleTag(articleTag: ArticleTag) {
    tag_name_text_view.text = articleTag.id
    followers_count_text_view.text = articleTag.followers_count.toString() + " Followers"
    Glide.with(context).load(articleTag.icon_url).into(tag_image_view)
  }
}