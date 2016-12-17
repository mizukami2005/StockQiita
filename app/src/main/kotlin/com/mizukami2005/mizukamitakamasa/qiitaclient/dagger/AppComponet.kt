package com.mizukami2005.mizukamitakamasa.qiitaclient.dagger

import com.mizukami2005.mizukamitakamasa.qiitaclient.view.activity.ArticleActivity
import com.mizukami2005.mizukamitakamasa.qiitaclient.view.activity.ListTagActivity
import com.mizukami2005.mizukamitakamasa.qiitaclient.MainActivity
import com.mizukami2005.mizukamitakamasa.qiitaclient.fragment.ViewPageListFragment
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