package com.example.mizukamitakamasa.qiitaclient

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.client.QiitaClient
import com.example.mizukamitakamasa.qiitaclient.fragment.ViewPageListFragment
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.model.User
import com.example.mizukamitakamasa.qiitaclient.util.AnimatorUtils
import com.example.mizukamitakamasa.qiitaclient.util.PxDpUtil
import com.example.mizukamitakamasa.qiitaclient.view.ArticleView
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

class MainActivity : RxAppCompatActivity(), ViewPager.OnPageChangeListener {
  override fun onPageScrollStateChanged(state: Int) {
    Log.e("PageScrollStateChanged", "PageScrollStateChanged")
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    Log.e("onPageScrolled", "onPageScrolled")
  }

  override fun onPageSelected(position: Int) {
    Log.e("onPageSelected", "onPageSelected")
  }

  @Inject
  lateinit var articleClient: ArticleClient
  @Inject
  lateinit var qiitaClient: QiitaClient

  var authURL = ""
  var clientID = ""
  var redirectURL = ""
  var scope = ""
  var state = ""
  var clientSecret = ""

//  var progressBar: ProgressBar by Delegates.notNull()
//    private set

//  var queryEditText: EditText by Delegates.notNull()
//    private set

//  var searchButton: Button by Delegates.notNull()
//    private set

  var loginButton: Button by Delegates.notNull()
    private set

  var listAdapter: ArticleListAdapter by Delegates.notNull()
//    private set

  var pagerAdapter: PagerAdapter by Delegates.notNull()
    private set

//  var viewPager: ViewPager by Delegates.notNull()
//    private set

  var bottomTabLayout: TabLayout by Delegates.notNull()
    private set

  val favBackground: View by lazy {
    findViewById(R.id.fav_background) as View
  }

  val favButton: FloatingActionButton by lazy {
    findViewById(R.id.fab_add) as FloatingActionButton
  }

  val fabLoginLayout: LinearLayout by lazy {
    findViewById(R.id.fab_login_layout) as LinearLayout
  }

  val favLoginButton: FloatingActionButton by lazy {
    findViewById(R.id.fav_login) as FloatingActionButton
  }

  val animatorUtils: AnimatorUtils by lazy {
    AnimatorUtils()
  }

  var count: Int = 1

  enum class ButtonState {
    OPEN, CLOSE
  }

  var buttonState: ButtonState = ButtonState.CLOSE

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as QiitaClientApp).component.inject(this)
    setContentView(R.layout.activity_main)

    // ToolBarの設定
    val mToolBar: Toolbar = findViewById(R.id.toolbar) as Toolbar
    mToolBar.title = "QiitaClient"
    setSupportActionBar(mToolBar)

    // TabLayoutの設定
    val tabLayout: TabLayout = findViewById(R.id.tabs) as TabLayout
    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

    val viewPager: ViewPager = findViewById(R.id.pager) as ViewPager
    val tags: MutableList<String> = mutableListOf("Recently", "Ruby", "Rails")

    // BottomTabLayoutの設定
//    bottomTabLayout = findViewById(R.id.bottom_tab_layout) as TabLayout
//    bottomTabLayout.addTab(bottomTabLayout.newTab().setText("Home"))
//    bottomTabLayout.addTab(bottomTabLayout.newTab().setText("Tag"))
//    bottomTabLayout.addTab(bottomTabLayout.newTab().setText("Account"))

    val viewPagerAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
      override fun getItem(position: Int): Fragment {
        return ViewPageListFragment.newInstance(tags[position])
    }

      override fun getCount(): Int {
        return tags.size
      }

      override fun getPageTitle(position: Int): CharSequence {
        return tags[position]
      }
    }

    viewPager.addOnPageChangeListener(this)
    viewPager.adapter = viewPagerAdapter

    tabLayout.setupWithViewPager(viewPager)

    val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
    val token = data.getString("token", "")
    Log.e("token", token)

//    progressBar = findViewById(R.id.progress_bar) as ProgressBar
//    queryEditText = findViewById(R.id.query_edit_text) as EditText
//    searchButton = findViewById(R.id.search_button) as Button
//    loginButton = findViewById(R.id.login_button) as Button

    try {
      val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
      authURL = appInfo.metaData.getString("auth_url")
      clientID = appInfo.metaData.getString("client_id")
      redirectURL = appInfo.metaData.getString("redirect_url")
      scope = appInfo.metaData.getString("scope")
      state = appInfo.metaData.getString("status")
      clientSecret = appInfo.metaData.getString("client_secret")

    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
    }
//    process(articleClient.recently("$count"))
    favLoginButton.setOnClickListener {
      val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
      val token = data.getString("token", "")
      if (token.length == 0) {
        val intent = Intent(Intent.ACTION_VIEW, getAuthURL(authURL, clientID, scope, state))
        startActivity(intent)
      } else {
        toast("保存済み: $token")
      }
    }
//    searchButton.setOnClickListener {
//      process(articleClient.search("$count", queryEditText.text.toString()))
//    }

//    val buttonState = ButtonState.CLOSE
    favButton.setOnClickListener {
      val iconWhile = PxDpUtil().dpToPx(applicationContext, 66)

      if (buttonState == ButtonState.CLOSE) {
        fabOpen(iconWhile)
      } else {
        fabClose()
      }
    }

//    loginButton.setOnClickListener {
//      val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
//      val token = data.getString("token", "")
//      if (token.length == 0) {
//        val intent = Intent(Intent.ACTION_VIEW, getAuthURL(authURL, clientID, scope, state))
//        startActivity(intent)
//      } else {
//        toast("保存済み: $token")
//      }
//    }
  }

  override fun onResume() {
    super.onResume()

    val intent = intent
    val action = intent.action
    Log.e("sample", "sample" + Intent.ACTION_VIEW)

    if (Intent.ACTION_VIEW.equals(action)) {
      val uri: Uri? = intent.data
      if (uri != null) {
        val code: String = uri.getQueryParameter("code")
        val state: String = uri.getQueryParameter("state")
        var token: String = ""
        Log.e("code", code)
        Log.e("state", state)
        Log.e("uri", uri.toString())
        if (state.equals("bb17785d811bb1913ef54b0a7657de780defaa2d")) {
          Log.e("success", "success")
          var map = HashMap<String, String>()
          map.put("client_id", clientID)
          map.put("client_secret", clientSecret)
          map.put("code", code)
          Log.e("aaaaaaaaaaaaaaa", token)
          qiitaClient.access(map)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doAfterTerminate {
                Log.e("aaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaa")
                Log.e("bbbbbbbbbbbbbbbbbbbbb", token)
                qiitaClient.getUser("Bearer $token")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate { }
                    .bindToLifecycle(this)
                    .subscribe({
                      Log.e("get User", "User:" + it)
                      toast("User: $it")
                    }, {
                      Log.e("no get User", "No:" + it)
                    })
              }
              .bindToLifecycle(this)
              .subscribe({
                Log.e("finish", it.token)
                token = it.token
                val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
                val editor = data.edit()
                editor.putString("token", token)
                editor.apply()
                toast("Finish: $it")
              }, {
                Log.e("エラー", "エラー" + it)
                toast("エラー: $it")
              })
        }
      }
    }
  }

  //qiita authのurlを作成
  private fun getAuthURL(authURL: String, clientID: String, scope: String, status: String): Uri =
      Uri.parse("$authURL?client_id=$clientID&scope=$scope&state=$status")

  // ダミー記事を生成するメソッド
  private fun dummyArticle(title: String, userName: String): Article =
      Article(id = "",
          title = title,
          url = "https://kotlinlang.org/",
          user = User(id = "", name = userName, profileImageUrl = ""))

  private fun fabOpen(iconWhile: Float) {
    fabLoginLayout.visibility = View.VISIBLE
    var anim = ObjectAnimator.ofFloat(fabLoginLayout, "translationY", -iconWhile)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fabLoginLayout, "alpha", 0f, 1f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(favButton, "rotation", 90f)
    anim.setDuration(200)
    anim.start()

    buttonState = ButtonState.OPEN
    favBackground.visibility = View.VISIBLE
  }

  private fun fabClose() {
    var anim = ObjectAnimator.ofFloat(fabLoginLayout, "translationY", 0f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fabLoginLayout, "alpha", 1f, 0f)
    anim.setDuration(200)
    anim.start()


    anim = ObjectAnimator.ofFloat(favButton, "rotation", 45f)
    anim.setDuration(200)
    anim.start()

    buttonState = ButtonState.CLOSE
    favBackground.visibility = View.GONE
  }

  // 通信処理
//  private fun process(observable: Observable<Array<Article>>) {
//    Log.e("ddddddddddddddd","dddddddddddddddd")
//    count = 1
//    progressBar.visibility = View.VISIBLE
//    observable
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .doAfterTerminate { progressBar.visibility = View.GONE }
//        .bindToLifecycle(this)
//        .subscribe({
//          Log.e("regggggggggggggggg", "argsgewsf" + this)
//          listAdapter.articles = it
//          listAdapter.notifyDataSetChanged()
//        }, {
//          toast("エラー: $it")
//        })
//  }
}
