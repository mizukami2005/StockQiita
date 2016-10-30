package com.example.mizukamitakamasa.qiitaclient.dagger

import com.example.mizukamitakamasa.qiitaclient.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by mizukamitakamasa on 2016/10/01.
 */

@Component(modules = arrayOf(ClientModule::class))
@Singleton
interface AppComponet {

    fun inject(mainActivity: MainActivity)
}