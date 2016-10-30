package com.example.mizukamitakamasa.qiitaclient

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.example.mizukamitakamasa.qiitaclient.client.QiitaClient
import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.model.User
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

class MainActivity : RxAppCompatActivity() {

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

    var progressBar: ProgressBar by Delegates.notNull()
        private set

    var queryEditText: EditText by Delegates.notNull()
        private set

    var searchButton: Button by Delegates.notNull()
        private set

    var loginButton: Button by Delegates.notNull()
        private set

    var listAdapter: ArticleListAdapter by Delegates.notNull()
        private set

    var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as QiitaClientApp).component.inject(this)
        setContentView(R.layout.activity_main)

        val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
        val token = data.getString("token", "")
        Log.e("token", token)

        val listView: ListView = findViewById(R.id.list_view) as ListView
        progressBar = findViewById(R.id.progress_bar) as ProgressBar
        queryEditText = findViewById(R.id.query_edit_text) as EditText
        searchButton = findViewById(R.id.search_button) as Button
        loginButton = findViewById(R.id.login_button) as Button

        listAdapter = ArticleListAdapter(application)

        listView.adapter = listAdapter
        listView.setOnItemClickListener { adapterView, view, position, id ->
            val article = listAdapter.articles[position]
            ArticleActivity.intent(this, article).let { startActivity(it) }
        }

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
        process(articleClient.recently("$count"))
//        articleClient.checkStock("Bearer $token","0f6d7a7778c7b06220f5")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doAfterTerminate {  }
//            .bindToLifecycle(this)
//            .subscribe({
//                toast("$it")
//            }, {
//                toast("エラー: $it")
//            })


        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            //            var count: Int = 1
            var isLoading: Boolean = true

            override fun onScroll(absListView: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount && isLoading) {
                    Log.e("latList", "latList")
                    count++
                    // データ取得処理
                    isLoading = false
                    if (queryEditText.text.toString().length != 0) {
                        Log.e("search", "search")
                        articleClient.search("$count", queryEditText.text.toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doAfterTerminate {
                                    isLoading = true
                                }
                                .bindToLifecycle(this@MainActivity)
                                .subscribe({
                                    listAdapter.addList(it)
                                    listAdapter.notifyDataSetChanged()
                                    var position = listView.firstVisiblePosition
                                    var yOffset = listView.getChildAt(0).top
                                    listView.setSelectionFromTop(position, yOffset)
                                }, {
                                    toast("エラー: $it")
                                })
                    } else {
                        Log.e("recently", "recently")
                        articleClient.recently("$count")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doAfterTerminate {
                                    isLoading = true
                                }
                                .bindToLifecycle(this@MainActivity)
                                .subscribe({
                                    listAdapter.addList(it)
                                    listAdapter.notifyDataSetChanged()
                                }, {
                                    toast("エラー: $it")
                                })
                    }

                }

            }

            override fun onScrollStateChanged(absListView: AbsListView?, scrollState: Int) {
            }
        })

        searchButton.setOnClickListener {
            process(articleClient.search("$count", queryEditText.text.toString()))
        }

        loginButton.setOnClickListener {
            val data = getSharedPreferences("DataToken", Context.MODE_PRIVATE)
            val token = data.getString("token", "")
            if (token.length == 0) {
                val intent = Intent(Intent.ACTION_VIEW, getAuthURL(authURL, clientID, scope, state))
                startActivity(intent)
            } else {
                toast("保存済み: $token")
            }
        }
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

    // 通信処理
    private fun process(observable: Observable<Array<Article>>) {
        count = 1
        progressBar.visibility = View.VISIBLE
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { progressBar.visibility = View.GONE }
                .bindToLifecycle(this)
                .subscribe({
                    listAdapter.articles = it
                    listAdapter.notifyDataSetChanged()
                }, {
                    toast("エラー: $it")
                })
    }
}