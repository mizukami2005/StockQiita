package com.example.mizukamitakamasa.qiitaclient

import android.app.Application
import com.example.mizukamitakamasa.qiitaclient.dagger.AppComponet
import com.example.mizukamitakamasa.qiitaclient.dagger.DaggerAppComponet

/**
 * Created by mizukamitakamasa on 2016/10/01.
 */

class QiitaClientApp : Application() {

    val component: AppComponet by lazy {
        DaggerAppComponet.create()
    }
}
