package com.mizukami2005.mizukamitakamasa.qiitaclient.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.mizukami2005.mizukamitakamasa.qiitaclient.R
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.Article
import kotlinx.android.synthetic.main.view_article.view.*

/**
 * Created by mizukamitakamasa on 2016/09/25.
 */

class ArticleView : FrameLayout {

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
    LayoutInflater.from(context).inflate(R.layout.view_article, this)
  }

  fun setArticle(article: Article, isArticleActivivty: Boolean = false) {
    title_text_view.text = article.title
    user_id_text_view.text = article.user.id
    if (isArticleActivivty) {
      title_text_view.setTextColor(ContextCompat.getColor(context, R.color.fab_background))
      user_id_text_view.setTextColor(ContextCompat.getColor(context, R.color.fab_background))
    }
    Glide.with(context).load(article.user.profileImageUrl).into(profile_image_view)
  }
}