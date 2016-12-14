package com.example.mizukamitakamasa.qiitaclient.dagger

import com.example.mizukamitakamasa.qiitaclient.view.activity.ArticleActivity
import com.example.mizukamitakamasa.qiitaclient.view.activity.ListTagActivity
import com.example.mizukamitakamasa.qiitaclient.MainActivity
import com.example.mizukamitakamasa.qiitaclient.fragment.ViewPageListFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by mizukamitakamasa on 2016/10/01.
 */

@Component(modules = arrayOf(ClientModule::class))
@Singleton
interface AppComponet {

  fun inject(mainActivity: MainActivity)
  fun inject(viewPageListFragment: ViewPageListFragment)
  fun inject(articleActivity: ArticleActivity)
  fun inject(listTagActivity: ListTagActivity)
}