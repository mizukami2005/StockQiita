package com.example.mizukamitakamasa.qiitaclient

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.webkit.WebView
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.view.ArticleView
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ArticleActivity : AppCompatActivity() {

  @Inject
  lateinit var articleClient: ArticleClient

  companion object {

    private const val ARTICLE_EXTRA: String = "article"

    fun intent(context: Context, article: Article): Intent =
        Intent(context, ArticleActivity::class.java)
            .putExtra(ARTICLE_EXTRA, article)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as QiitaClientApp).component.inject(this)
    setContentView(R.layout.activity_article)

//    val collapsongToolBarLayout = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
    val stockButton = findViewById(R.id.stock_button) as FloatingActionButton
    val articleView = findViewById(R.id.article_view) as ArticleView
    val webView = findViewById(R.id.web_view) as WebView

    val article: Article = intent.getParcelableExtra(ARTICLE_EXTRA)
    articleView.setArticle(article)
    webView.loadUrl(article.url)

    stockButton.setOnClickListener {
      val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
      val token = data.getString("token", "")
      if (token.length != 0) {
        articleClient.stock("Bearer $token", article.id)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doAfterTerminate {
            //todo ボタンの色を変える
          }
          .bindToLifecycle(MainActivity())
          .subscribe({
            toast("stock: $it")
          }, {
            toast("stock出来ませんでした: $it")
          })
      }
    }
  }
}
