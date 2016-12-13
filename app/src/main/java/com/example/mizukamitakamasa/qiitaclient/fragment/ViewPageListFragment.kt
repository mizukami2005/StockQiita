package com.example.mizukamitakamasa.qiitaclient.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import com.example.mizukamitakamasa.qiitaclient.*
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
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

  var count = 1
  var isLoading = true

  private fun getItems(observable: Observable<Array<Article>>) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate {
      isLoading = true
      activity.progress_bar.visibility = View.GONE
    }
    .bindToLifecycle(MainActivity())
    .subscribe({
      listAdapter.articles = it
      listAdapter.notifyDataSetChanged()
    }, {
      context.toast(getString(R.string.error_message))
    })
  }

  private fun getAddItems(observable: Observable<Array<Article>>) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate {
      isLoading = true
      activity.progress_bar.visibility = View.GONE
    }
    .bindToLifecycle(MainActivity())
    .subscribe({
      listAdapter.addList(it)
      listAdapter.notifyDataSetChanged()
      var position = listView.firstVisiblePosition
      var yOffset = listView.getChildAt(0).top
      listView.setSelectionFromTop(position, yOffset)
    }, {
      context.toast(getString(R.string.error_message))
    })
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    (context.applicationContext as QiitaClientApp).component.inject(this)

    listView = inflater.inflate(R.layout.fragment_view_page_list, container, false) as ListView
    init()

    return listView
  }

  companion object {
    fun newInstance(tag: String): ViewPageListFragment {
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
      activity.progress_bar.visibility = View.VISIBLE
      getItems(articleClient.recently("$count"))
    } else {
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
          activity.progress_bar.visibility = View.VISIBLE
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