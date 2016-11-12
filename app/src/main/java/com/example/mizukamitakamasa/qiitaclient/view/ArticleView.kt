package com.example.mizukamitakamasa.qiitaclient.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mizukamitakamasa.qiitaclient.R
import com.example.mizukamitakamasa.qiitaclient.bindView
import com.example.mizukamitakamasa.qiitaclient.model.Article

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

  val profileImageView: ImageView by bindView(R.id.profile_image_view)

  val titleTextView: TextView by bindView(R.id.title_text_view)

  val userIdTextView: TextView by bindView(R.id.user_id_text_view)

//  val folderImageView: ImageButton by bindView(R.id.folder_image_view)

  init {
    LayoutInflater.from(context).inflate(R.layout.view_article, this)
//    folderImageView.setOnClickListener {
//      Log.e("Helowween", "Helowween")
//    }
  }

  fun setArticle(article: Article) {
    titleTextView.text = article.title
    userIdTextView.text = article.user.id
    Glide.with(context).load(article.user.profileImageUrl).into(profileImageView)
//    folderImageView.setImageResource(R.mipmap.ic_folder_white)
//    folderImageView.setBackgroundColor(Color.argb(255, 89, 187, 12))
  }
}