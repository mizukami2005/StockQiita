package com.example.mizukamitakamasa.qiitaclient

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.view.ArticleView
import com.mukesh.MarkdownView
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ArticleActivity : AppCompatActivity() {

  @Inject
  lateinit var articleClient: ArticleClient

  var checkStock = false

  val stockButton: FloatingActionButton by lazy {
    findViewById(R.id.stock_button) as FloatingActionButton
  }

  val articleView: ArticleView by lazy {
    findViewById(R.id.article_view) as ArticleView
  }

  val markdownView: MarkdownView by lazy {
    findViewById(R.id.markdown_view) as MarkdownView
  }

  val appBarLayout: AppBarLayout by lazy {
    findViewById(R.id.app_bar) as AppBarLayout
  }

  val collapsongToolBarLayout: CollapsingToolbarLayout by lazy {
    findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
  }

  val toolBar: Toolbar by lazy {
    findViewById(R.id.toolbar) as Toolbar
  }

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

    val article: Article = intent.getParcelableExtra(ARTICLE_EXTRA)
    setSupportActionBar(toolBar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    articleView.setArticle(article, true)
    markdownView.setMarkDownText(article.body)

    appBarLayout.addOnOffsetChangedListener { appBarLayout, offset ->
      collapsongToolBarLayout.title = ""
      if (-offset + toolBar.height == collapsongToolBarLayout.height) {
        collapsongToolBarLayout.title = article.title
        collapsongToolBarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(applicationContext, R.color.fab_background))
      }
    }

    val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
    val token = data.getString("token", "")

    processCheck(articleClient.checkStock("Bearer $token", article.id))

    stockButton.setOnClickListener {

      if (token.length != 0 && !checkStock) {
        val stateList = ColorStateList(
            arrayOf<IntArray>(intArrayOf()), intArrayOf(Color.parseColor("#C9302C"))
        )
        stockButton.backgroundTintList = stateList
        stockButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_folder_white, null))
        processStock(articleClient.stock("Bearer $token", article.id), true)
      } else if (token.length != 0 && checkStock) {
        val stateList = ColorStateList(
            arrayOf<IntArray>(intArrayOf()), intArrayOf(Color.parseColor("#FFFFFF"))
        )
        stockButton.backgroundTintList = stateList
        stockButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_folder_green, null))
        processStock(articleClient.unStock("Bearer $token", article.id), false)
      } else {
        toast("ログインしていません")
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> finish()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun processCheck(observable: Observable<String>) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
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
  }

  private fun processStock(observable: Observable<String>, isStock: Boolean) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .bindToLifecycle(MainActivity())
    .subscribe({
      if (isStock) {
        checkStock = true
        toast("ストックしました")
      } else {
        checkStock = false
        toast("ストックを解除しました")
      }
    }, {
      if (isStock) {
        checkStock = false
        toast("ストック出来ませんでした")
      } else {
        checkStock = true
        toast("エラー: $it")
      }
    })
  }
}
