package com.example.mizukamitakamasa.qiitaclient.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import com.example.mizukamitakamasa.qiitaclient.*
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.util.TagUtils
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.zip.Inflater
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by mizukamitakamasa on 2016/11/03.
 */
class ViewPageListFragment: Fragment() {

  @Inject
  lateinit var articleClient: ArticleClient

  val listAdapter: ArticleListAdapter by lazy {
    ArticleListAdapter(context)
  }

  var listView: ListView by Delegates.notNull()
    private set

  val progressBar: ProgressBar by lazy {
    activity.findViewById(R.id.progress_bar) as ProgressBar
  }

  var count = 1
  var isLoading = true

  private fun getItems(observable: Observable<Array<Article>>) {
    observable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doAfterTerminate {
        isLoading = true
        progressBar.visibility = View.GONE
      }
      .bindToLifecycle(MainActivity())
      .subscribe({
        listAdapter.articles = it
        listAdapter.notifyDataSetChanged()
      }, {
        Log.e("error", "error: $it")
      })
    Log.e("getItems", "getItems")
  }

  private fun getAddItems(observable: Observable<Array<Article>>) {
    observable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doAfterTerminate {
        isLoading = true
        progressBar.visibility = View.GONE
      }
      .bindToLifecycle(MainActivity())
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

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    (context.applicationContext as QiitaClientApp).component.inject(this)
    Log.e("onCreateView", "onCreateView")

    listView = inflater.inflate(R.layout.fragment_view_page_list, container, false) as ListView
    init()

    return listView
  }

  companion object {
    fun newInstance(tag: String): ViewPageListFragment {
      Log.e("newInstance", "newInstance")
      val args = Bundle()
      args.putString("tag", tag)
      val fragment = ViewPageListFragment()
      fragment.arguments = args
      return fragment
    }
  }

  fun init() {
    var tag = arguments.getString("tag", "Ruby")
    if (tag == "Recently") {
      Log.e("Recently", "Recently")
      progressBar.visibility = View.VISIBLE
      getItems(articleClient.recently("$count"))
    } else {
      Log.e("else", "else")
      getItems(articleClient.tagItems(tag, "$count"))
    }

    listView.adapter = listAdapter
    listView.setOnItemClickListener { adapterView, view, position, id ->
      val article = listAdapter.articles[position]
      ArticleActivity.intent(context, article).let { startActivity(it) }
    }
    listView.setOnScrollListener(object : AbsListView.OnScrollListener {
      override fun onScroll(absListView: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount && isLoading) {
          isLoading = false
          progressBar.visibility = View.VISIBLE
          if (tag == "Recently") {
            count++
            getAddItems(articleClient.recently("$count"))
          } else {
            count++
            getAddItems(articleClient.tagItems(tag, "$count"))
          }
        }
      }

      override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
      }
    })
  }
}