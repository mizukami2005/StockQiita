package com.mizukami2005.mizukamitakamasa.qiitaclient

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.MaterialIcons
import com.joanzapata.iconify.fonts.MaterialModule
import com.mizukami2005.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.mizukami2005.mizukamitakamasa.qiitaclient.client.QiitaClient
import com.mizukami2005.mizukamitakamasa.qiitaclient.fragment.ViewPageListFragment
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.RealmArticle
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.RealmUser
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.ResponseToken
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.User
import com.mizukami2005.mizukamitakamasa.qiitaclient.util.PxDpUtil
import com.mizukami2005.mizukamitakamasa.qiitaclient.util.Config
import com.mizukami2005.mizukamitakamasa.qiitaclient.util.TagUtils
import com.mizukami2005.mizukamitakamasa.qiitaclient.view.activity.ListTagActivity
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MainActivity : RxAppCompatActivity(), ViewPager.OnPageChangeListener {

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

  enum class ButtonState {
    OPEN, CLOSE
  }

  var buttonState: ButtonState = ButtonState.CLOSE

  val TOKEN_PREFERENCES_NAME = "DataToken"
  val TOKEN = "token"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as QiitaClientApp).component.inject(this)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    tabs.tabMode = TabLayout.MODE_SCROLLABLE
    val data = getSharedPreferences(TOKEN_PREFERENCES_NAME, Context.MODE_PRIVATE)
    val token = data.getString(TOKEN, "")
    if (token.length != 0) {
      login_text.text = getString(R.string.logout)
    }

    init()

    try {
        authURL = Config().authUrl()
        clientID = Config().clientId()
        redirectURL = Config().redirectUrl()
        scope = Config().scope()
        state = Config().state()
        clientSecret = Config().clientSecret()
    } catch (e: Exception) {
      e.printStackTrace()
    }

    fab_login.setOnClickListener {
      val token = data.getString(TOKEN, "")
      if (token.length == 0) {
        val intent = Intent(Intent.ACTION_VIEW, getAuthURL(authURL, clientID, scope, state))
        startActivity(intent)
      } else {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle(getString(R.string.alert_dialog_title))
        builder.setMessage(getString(R.string.alert_dialog_message))
        builder.setPositiveButton(getString(R.string.positive_button_text), DialogInterface.OnClickListener { dialogInterface, i ->
          val editor = data.edit()
          editor.putString(TOKEN, "")
          editor.apply()
          login_text.text = getString(R.string.login)
        })
        builder.setNegativeButton(getString(R.string.negative_button_text), null)
        builder.create().show()
      }
    }

    fab_tags_button.setOnClickListener {
      val tags = arrayListOf("")
      val requestCode = 1001
      ListTagActivity.intent(applicationContext, tags).let { startActivityForResult(it, requestCode) }
    }

    fab_add.setOnClickListener {
      val iconWhile = PxDpUtil().dpToPx(applicationContext, 66)
      if (buttonState == ButtonState.CLOSE) {
        fabOpen(iconWhile)
      } else {
        fabClose()
      }
    }

    fab_background.setOnTouchListener { view, motionEvent ->
      true
    }

    Realm.init(this)
    Iconify.with(MaterialModule())
    fab_add.setImageDrawable(IconDrawable(this, MaterialIcons.md_add).colorRes(R.color.fab_background))
    fab_login.setImageDrawable(IconDrawable(this, MaterialIcons.md_person).colorRes(R.color.fab_background))
    fab_tags_button.setImageDrawable(IconDrawable(this, MaterialIcons.md_local_offer).colorRes(R.color.fab_background))
  }

  override fun onResume() {
    super.onResume()

    val intent = intent
    val action = intent.action
    val data = getSharedPreferences(TOKEN_PREFERENCES_NAME, Context.MODE_PRIVATE)
    val token = data.getString(TOKEN, "")

    if (Intent.ACTION_VIEW.equals(action) && token.length == 0) {
      val uri: Uri? = intent.data
      if (uri != null) {
        val code: String = uri.getQueryParameter("code")
        val state: String = uri.getQueryParameter("state")
        var token: String = ""
        if (state.equals("bb17785d811bb1913ef54b0a7657de780defaa2d")) {
          var map = HashMap<String, String>()
          map.put("client_id", clientID)
          map.put("client_secret", clientSecret)
          map.put("code", code)
          getToken(qiitaClient.access(map))
        }
      }
    }
  }

  override fun onPageScrollStateChanged(state: Int) {
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
  }

  override fun onPageSelected(position: Int) {
  }

  private fun getAuthURL(authURL: String, clientID: String, scope: String, status: String): Uri =
      Uri.parse("$authURL?client_id=$clientID&scope=$scope&state=$status")

  private fun fabOpen(iconWhile: Float) {
    fab_login_layout.visibility = View.VISIBLE
    var anim = ObjectAnimator.ofFloat(fab_login_layout, "translationY", -iconWhile)
    anim.setDuration(200)
    anim.start()

    fab_tags_layout.visibility = View.VISIBLE
    anim = ObjectAnimator.ofFloat(fab_tags_layout, "translationY", -iconWhile * 2)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fab_login_layout, "alpha", 0f, 1f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fab_tags_layout, "alpha", 0f, 1f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fab_add, "rotation", 45f)
    anim.setDuration(200)
    anim.start()

    buttonState = ButtonState.OPEN
    fab_background.visibility = View.VISIBLE
  }

  private fun fabClose() {
    var anim = ObjectAnimator.ofFloat(fab_login_layout, "translationY", 0f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fab_tags_layout, "translationY", 0f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fab_login_layout, "alpha", 1f, 0f)
    anim.setDuration(200)
    anim.start()

    anim = ObjectAnimator.ofFloat(fab_tags_layout, "alpha", 1f, 0f)
    anim.setDuration(200)
    anim.start()


    anim = ObjectAnimator.ofFloat(fab_add, "rotation", 90f)
    anim.setDuration(200)
    anim.start()

    buttonState = ButtonState.CLOSE
    fab_background.visibility = View.GONE
  }

  private fun init() {
    val tagLists = TagUtils().loadName(applicationContext, "TAG")
    val tags: MutableList<String> = mutableListOf("Recently")
    for (tag in tagLists) {
      tags += tag
    }

    val viewPagerAdapter: FragmentStatePagerAdapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
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

    pager.addOnPageChangeListener(this)
    pager.adapter = viewPagerAdapter
    tabs.setupWithViewPager(pager)
  }

  public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (requestCode == 1001) {
      if (resultCode == Activity.RESULT_OK) {
        init()
        fabClose()
      }
    }
  }

  private fun getToken(observable: Observable<ResponseToken>) {
    var token: String = ""
    val data = getSharedPreferences(TOKEN_PREFERENCES_NAME, Context.MODE_PRIVATE)
    val editor = data.edit()
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate {
      if (token.length != 0) {
        getQiitaUser(qiitaClient.getUser("Bearer $token"))
      }
    }
    .bindToLifecycle(this)
    .subscribe({
      token = it.token
      editor.putString(TOKEN, token)
      editor.apply()
    })
  }

  private fun getQiitaUser(observable: Observable<User>) {
    observable
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doAfterTerminate {
      login_text.text = getString(R.string.logout)
    }
    .bindToLifecycle(this)
    .subscribe({
      toast(applicationContext.getString(R.string.success_login_message))
    }, {
      toast(applicationContext.getString(R.string.error_message))
    })
  }
}
