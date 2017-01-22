package com.mizukami2005.mizukamitakamasa.qiitaclient.dagger

import com.google.gson.*
import com.mizukami2005.mizukamitakamasa.qiitaclient.client.ArticleClient
import com.mizukami2005.mizukamitakamasa.qiitaclient.client.QiitaClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ClientModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    return f.declaredClass == RealmObject::class.java
                }

                override fun shouldSkipClass(clazz: Class<*>): Boolean {
                    return false
                }
            })
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
            .baseUrl("https://qiita.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideArticleClient(retrofit: Retrofit): ArticleClient =
            retrofit.create(ArticleClient::class.java)

    @Provides
    @Singleton
    fun provideQiitaClient(retrofit: Retrofit): QiitaClient =
            retrofit.create(QiitaClient::class.java)
}
