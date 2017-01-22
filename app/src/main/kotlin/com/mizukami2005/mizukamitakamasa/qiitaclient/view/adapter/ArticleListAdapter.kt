package com.mizukami2005.mizukamitakamasa.qiitaclient.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.Article
import com.mizukami2005.mizukamitakamasa.qiitaclient.view.ArticleView
import java.util.*

/**
 * Created by mizukamitakamasa on 2016/09/25.
 */

class ArticleListAdapter(private val context: Context) : BaseAdapter() {

  internal var articles = emptyArray<Article>()

  override fun getCount(): Int = articles.size

  override fun getItem(position: Int): Any? = articles[position]

  override fun getItemId(position: Int): Long = 0

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
      ((convertView as? ArticleView) ?: ArticleView(context)).apply {
        setArticle(articles[position])
      }

  fun addList(list: Array<Article>) {
    articles += list
  }

  fun clear() {
    articles = emptyArray<Article>()
  }
}