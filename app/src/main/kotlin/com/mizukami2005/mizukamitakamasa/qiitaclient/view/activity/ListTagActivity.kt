package com.mizukami2005.mizukamitakamasa.qiitaclient.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.AbsListView
import com.mizukami2005.mizukamitakamasa.qiitaclient.QiitaClientApp
import com.mizukami2005.mizukamitakamasa.qiitaclient.R
import com.mizukami2005.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.ArticleTag
import com.mizukami2005.mizukamitakamasa.qiitaclient.toast
import com.mizukami2005.mizukamitakamasa.qiitaclient.util.TagUtils
import com.mizukami2005.mizukamitakamasa.qiitaclient.view.adapter.ArticleTagListAdapter
import kotlinx.android.synthetic.main.activity_list_tag.*
import kotlinx.android.synthetic.main.view_article_tag.*
import kotlinx.android.synthetic.main.view_article_tag.view.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class ListTagActivity : AppCompatActivity() {

  @Inject
  lateinit var articleClient: ArticleClient

  val listAdapter: ArticleTagListAdapter by lazy {
    ArticleTagListAdapter(applicationContext)
  }

  var count = 1
  var isLoading = true

  val checkTagList: MutableSet<String> = mutableSetOf()

  companion object {

    private var TAG_EXTRA: ArrayList<String> = arrayListOf()

    fun intent(context: Context, tags: ArrayList<String>): Intent =
        Intent(context, ListTagActivity::class.java)
          .putStringArrayListExtra("${TAG_EXTRA}", tags)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as QiitaClientApp).component.inject(this)
    setContentView(R.layout.activity_list_tag)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val saveTagLists = TagUtils().loadName(applicationContext, "TAG")
    for (tag in saveTagLists) {
      checkTagList += tag
    }

    getTags(articleClient.tags("$count"))
    list_view.adapter = listAdapter

    list_view.setOnItemClickListener { adapterView, view, position, id ->
      val articleTag = listAdapter.articleTags[position]
      if (view.tag_check_box.isChecked) {
        view.tag_check_box.isChecked = false
        checkTagList -= articleTag.id
      } else {
        view.tag_check_box.isChecked = true
        checkTagList += articleTag.id
      }
      TagUtils().saveName(applicationContext, "TAG", checkTagList)
    }

    home_button.setOnClickListener {
      val intent = intent
      setResult(Activity.RESULT_OK, intent)
      finish()
    }

    list_view.setOnScrollListener(object : AbsListView.OnScrollListener {
      override fun onScroll(absListView: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount && isLoading && count <= 4) {
          isLoading = false
          count++
          getAddTagItems(articleClient.tags("$count"))
        }
      }

      override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
      }
    })
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      val intent = intent
      setResult(Activity.RESULT_OK, intent)
      finish()
      return true
    }
    return false
  }

  private fun getTags(observable: Observable<Array<ArticleTag>>) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({
      listAdapter.articleTags = it
      listAdapter.notifyDataSetChanged()
    }, {
      toast(getString(R.string.error_message))
    })
  }

  private fun getAddTagItems(observable: Observable<Array<ArticleTag>>) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate {
      isLoading = true
    }
    .subscribe({
      listAdapter.addList(it)
      listAdapter.notifyDataSetChanged()
      var position = list_view.firstVisiblePosition
      var yOffset = list_view.getChildAt(0).top
      list_view.setSelectionFromTop(position, yOffset)
    }, {
      toast(getString(R.string.error_message))
    })
  }
}
