package com.example.mizukamitakamasa.qiitaclient.client

import com.example.mizukamitakamasa.qiitaclient.model.Article
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * Created by mizukamitakamasa on 2016/09/25.
 */
interface ArticleClient {

    @GET("/api/v2/items")
    fun search(@Query("page") number: String = "1", @Query("query") query: String): Observable<Array<Article>>

    @GET("/api/v2/items")
    fun recently(@Query("page") number: String = "1") : Observable<Array<Article>>

    @GET("/api/v2/items/{item_id}/stock")
    fun checkStock(@Header("Authorization") authorization: String, @Path("item_id") item_id: String) : Observable<String>
}