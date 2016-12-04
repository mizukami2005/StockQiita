package com.example.mizukamitakamasa.qiitaclient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.KeyEvent
import android.widget.AbsListView
import android.widget.CheckBox
import android.widget.ListView
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.model.ArticleTag
import com.example.mizukamitakamasa.qiitaclient.util.TagUtils
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import org.json.JSONArray
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

class ListTagActivity : AppCompatActivity() {

  @Inject
  lateinit var articleClient: ArticleClient

  val listAdapter: ArticleTagListAdapter by lazy {
    ArticleTagListAdapter(applicationContext)
  }

  val listView: ListView by lazy {
    findViewById(R.id.list_view) as ListView
  }

  val homeButton: FloatingActionButton by lazy {
    findViewById(R.id.home_button) as FloatingActionButton
  }

  var count = 1
  var isLoading = true

  val checkTagList: MutableSet<String> = mutableSetOf()

  companion object {

    private var TAG_EXTRA: ArrayList<String> = arrayListOf()

    fun intent(context: Context, tags: ArrayList<String>): Intent =
        Intent(context, ListTagActivity::class.java)
          .putStringArrayListExtra("$TAG_EXTRA", tags)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as QiitaClientApp).component.inject(this)
    setContentView(R.layout.activity_list_tag)

    val saveTagLists = TagUtils().loadName(applicationContext, "TAG")
    for (tag in saveTagLists) {
      checkTagList += tag
    }

//    ListView = findViewById(R.id.list_view) as ListView
    getTags(articleClient.tags("$count"))
    listView.adapter = listAdapter

    listView.setOnItemClickListener { adapterView, view, position, id ->
      val articleTag = listAdapter.articleTags[position]
      val item = listView.getItemAtPosition(position)
      val checkBox = view.findViewById(R.id.tag_check_box) as CheckBox
      Log.e("checkbox", "checlbox: $checkBox")
      if (checkBox.isChecked) {
        checkBox.isChecked = false
        checkTagList -= articleTag.id
      } else {
        checkBox.isChecked = true
        checkTagList += articleTag.id
      }
      Log.e("item", "$item")
      Log.e("TAG", articleTag.id)
      Log.e("checkTagList", checkTagList.toString())
      TagUtils().saveName(applicationContext, "TAG", checkTagList)
    }

    homeButton.setOnClickListener {
      val intent = intent
      setResult(Activity.RESULT_OK, intent)
      finish()
    }

    listView.setOnScrollListener(object : AbsListView.OnScrollListener {
      override fun onScroll(absListView: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount && isLoading) {
          isLoading = false
          count++
          getAddTagItems(articleClient.tags("$count"))
        }
      }

      override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {

      }
    })
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      Log.e("back", "back")
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
    .doAfterTerminate { }
    .subscribe({
      listAdapter.articleTags = it
      listAdapter.notifyDataSetChanged()
    }, {
      Log.e("error", "error: $it")
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
      var position = listView.firstVisiblePosition
      var yOffset = listView.getChildAt(0).top
      listView.setSelectionFromTop(position, yOffset)
    }, {
      Log.e("error", "error: $it")
    })
  }

  private fun saveTagNameList(context: Context, key: String, values: MutableSet<String>) {
    val prefs = context.getSharedPreferences("tag", Context.MODE_PRIVATE)
    var editor = prefs.edit()
    editor.putStringSet(key, values)
    editor.apply()
  }
}
