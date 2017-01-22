package com.mizukami2005.mizukamitakamasa.qiitaclient.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import com.mizukami2005.mizukamitakamasa.qiitaclient.*
import com.mizukami2005.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.Article
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.RealmArticle
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.RealmUser
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.User
import com.mizukami2005.mizukamitakamasa.qiitaclient.view.activity.ArticleActivity
import com.mizukami2005.mizukamitakamasa.qiitaclient.view.adapter.ArticleListAdapter
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_list_tag.*
import kotlinx.android.synthetic.main.activity_list_tag.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view_page_list.*
import kotlinx.android.synthetic.main.fragment_view_page_list.view.*
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
  var isRefresh = false

  private fun getItems(observable: Observable<List<RealmArticle>>, tag: String) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate {
      isLoading = true
      activity.progress_bar.visibility = View.GONE
      if (swipe_refresh.isRefreshing) {
        swipe_refresh.isRefreshing = false
      }
      listAdapter.articles = loadArticle(tag)
      listAdapter.notifyDataSetChanged()
    }
    .bindToLifecycle(MainActivity())
    .subscribe(
        { article ->
          Realm.getDefaultInstance().use {
            realm -> realm.executeTransaction {
              for (index in article.indices) {
                article.get(index).type = tag
              }
              realm.copyToRealmOrUpdate(article)
            }
          }
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
      var arrayArticle = emptyArray<Article>()
      for (index in it.indices) {
        if (!isExitArticle(it.get(index).id)) {
          arrayArticle += it.get(index)
        }
      }
      listAdapter.addList(arrayArticle)
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
    val view = inflater.inflate(R.layout.fragment_view_page_list, null)
    view.swipe_refresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE)

    view.swipe_refresh.setOnRefreshListener {
      isRefresh = true
      deleteSavedArticle()
      listAdapter.clear()
      listAdapter.notifyDataSetChanged()
      count = 1
      init()
    }
    listView = view.page_list_view
    init()

    return view
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
    val loadArticle = Realm.getDefaultInstance().where(RealmArticle::class.java).equalTo("type", tag).findAll()
    if (tag == "Recently") {
      if (isRefresh) {
        activity.progress_bar.visibility = View.VISIBLE
        getItems(articleClient.recentlySaveRealm("$count"), tag)
      } else if (loadArticle.size != 0) {
        listAdapter.articles = loadArticle(tag)
      } else {
        activity.progress_bar.visibility = View.VISIBLE
        getItems(articleClient.recentlySaveRealm("$count"), tag)
      }
    } else {
      if (isRefresh) {
        activity.progress_bar.visibility = View.VISIBLE
        getItems(articleClient.tagItemsSaveRealm(tag, "$count"), tag)
      } else if (loadArticle.size != 0) {
        listAdapter.articles = loadArticle(tag)
      } else {
        activity.progress_bar.visibility = View.VISIBLE
        getItems(articleClient.tagItemsSaveRealm(tag, "$count"), tag)
      }
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

  private fun loadArticle(tag: String): Array<Article> {
    val loadArticle = Realm.getDefaultInstance().where(RealmArticle::class.java).equalTo("type", tag).findAll().sort("createdAt", Sort.DESCENDING)
    var arrayArticle = emptyArray<Article>()
    for (index in loadArticle.indices) {
      var article = Article()
      article.id = loadArticle.get(index).id as String
      article.title = loadArticle.get(index).title as String
      article.url = loadArticle.get(index).url as String
      article.body = loadArticle.get(index).body as String
      article.user = User(loadArticle.get(index).user?.id as String, loadArticle.get(index).user?.name as String, loadArticle.get(index).user?.profileImageUrl as String)
      article.type = loadArticle.get(index).type as String
      article.createdAt = loadArticle.get(index).createdAt as String
      arrayArticle += article
    }
    return arrayArticle
  }

  private fun deleteSavedArticle() {
    val loadArticle = Realm.getDefaultInstance().where(RealmArticle::class.java).findAll()
    val loadUser = Realm.getDefaultInstance().where(RealmUser::class.java).findAll()

    Realm.getDefaultInstance().use {
      realm -> realm.executeTransaction {
      loadArticle.deleteAllFromRealm()
      loadUser.deleteAllFromRealm()
      }
    }
  }

  private fun isExitArticle(articleId: String): Boolean {
    val loadArticle = Realm.getDefaultInstance().where(RealmArticle::class.java).findAll()
    return loadArticle.any { it.id == articleId }
  }
}