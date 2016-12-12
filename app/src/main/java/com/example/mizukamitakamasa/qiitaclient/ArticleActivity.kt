package com.example.mizukamitakamasa.qiitaclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.TextView
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

  lateinit var article: Article

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

  val tooLBarTitle: TextView by lazy {
    findViewById(R.id.toolbar_text_view) as TextView
  }

  var isMenu = false

  lateinit var data: SharedPreferences
  lateinit var token: String

  val TOKEN_PREFERENCES_NAME = "DataToken"
  val TOKEN = "token"

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

    article = intent.getParcelableExtra(ARTICLE_EXTRA)
    setSupportActionBar(toolBar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    articleView.setArticle(article, true)
    markdownView.setMarkDownText(article.body)

    appBarLayout.addOnOffsetChangedListener { appBarLayout, offset ->
      collapsongToolBarLayout.title = ""
      tooLBarTitle.text = ""
      isMenu = false
      invalidateOptionsMenu()
      if (-offset + toolBar.height == collapsongToolBarLayout.height) {
        isMenu = true
        tooLBarTitle.text = article.title
        invalidateOptionsMenu()
      }
    }

    data = getSharedPreferences(TOKEN_PREFERENCES_NAME, Context.MODE_PRIVATE)
    token = data.getString(TOKEN, "")

    toolBar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener {
      override fun onMenuItemClick(item: MenuItem?): Boolean {
        changeStockUnstock()
        return true
      }
    })

    processCheck(articleClient.checkStock("Bearer $token", article.id))

    stockButton.setOnClickListener {
      changeStockUnstock()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    if (isMenu) {
      menuInflater.inflate(R.menu.menu_main, menu)
      if (checkStock) {
        menu.findItem(R.id.action_stock).setIcon(R.drawable.item_folder_red)
      }
    }
    return super.onCreateOptionsMenu(menu)
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
    }, {
      checkStock = false
    })
  }

  private fun processStock(observable: Observable<String>, isStock: Boolean) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate { invalidateOptionsMenu() }
    .bindToLifecycle(MainActivity())
    .subscribe({
      if (isStock) {
        checkStock = true
        val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vib.vibrate(100)
      } else {
        checkStock = false
      }
    }, {
      if (isStock) {
        checkStock = false
      } else {
        checkStock = true
      }
    })
  }

  private fun changeStockUnstock() {
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
      toast(getString(R.string.not_login_message))
    }
  }
}
