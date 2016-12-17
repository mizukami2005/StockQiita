package com.mizukami2005.mizukamitakamasa.qiitaclient

import android.app.Application
import com.mizukami2005.mizukamitakamasa.qiitaclient.dagger.AppComponet
import com.mizukami2005.mizukamitakamasa.qiitaclient.dagger.DaggerAppComponet

/**
 * Created by mizukamitakamasa on 2016/10/01.
 */

class QiitaClientApp : Application() {

  val component: AppComponet by lazy {
    DaggerAppComponet.create()
  }
}
