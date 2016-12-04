package com.example.mizukamitakamasa.qiitaclient

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.res.ResourcesCompat
import android.webkit.WebView
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.view.ArticleView
import com.mukesh.MarkdownView
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ArticleActivity : AppCompatActivity() {

  @Inject
  lateinit var articleClient: ArticleClient

  var checkStock = false

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
    val markdownView = findViewById(R.id.markdown_view) as MarkdownView

    val article: Article = intent.getParcelableExtra(ARTICLE_EXTRA)
    articleView.setArticle(article)
    markdownView.setMarkDownText(article.body)

    val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
    val token = data.getString("token", "")

    articleClient.checkStock("Bearer $token", article.id)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doAfterTerminate { }
      .bindToLifecycle(MainActivity())
      .subscribe({
        val stateList = ColorStateList(
            arrayOf<IntArray>(intArrayOf()), intArrayOf(Color.parseColor("#C9302C"))
        )
        stockButton.backgroundTintList = stateList
        stockButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_folder_white, null))
        checkStock = true
        toast("stock済み: $it")
      }, {
        checkStock = false
      })

    stockButton.setOnClickListener {

      if (token.length != 0 && !checkStock) {
        articleClient.stock("Bearer $token", article.id)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doAfterTerminate {
            val stateList = ColorStateList(
                arrayOf<IntArray>(intArrayOf()), intArrayOf(Color.parseColor("#C9302C"))
            )
            stockButton.backgroundTintList = stateList
            stockButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_folder_white, null))
          }
          .bindToLifecycle(MainActivity())
          .subscribe({
            checkStock = true
            toast("ストックしました")
          }, {
            checkStock = false
            toast("ストック出来ませんでした")
          })
      } else if (token.length != 0 && checkStock) {
        articleClient.unStock("Bearer $token", article.id)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doAfterTerminate {
            val stateList = ColorStateList(
                arrayOf<IntArray>(intArrayOf()), intArrayOf(Color.parseColor("#FFFFFF"))
            )
            stockButton.backgroundTintList = stateList
            stockButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_folder_green, null))
          }
          .bindToLifecycle(MainActivity())
          .subscribe({
            checkStock = false
            toast("ストックを解除しました")
          }, {
            checkStock = true
            toast("エラー: $it")
          })
      } else {
        toast("ログインしていません")
      }
    }
  }
}
