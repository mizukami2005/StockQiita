package com.example.mizukamitakamasa.qiitaclient.client

import com.example.mizukamitakamasa.qiitaclient.model.Article
import com.example.mizukamitakamasa.qiitaclient.model.ArticleTag
import retrofit2.http.*
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

    @GET("/api/v2/tags")
    fun tags(@Query("page") number: String = "1", @Query("sort") sort: String = "count") : Observable<Array<ArticleTag>>

    @GET("/api/v2/tags/{tag_id}/items")
    fun tagItems(@Path("tag_id") tag_id: String, @Query("page") number: String = "1") : Observable<Array<Article>>

    @PUT("/api/v2/items/{item_id}/stock")
    fun stock(@Header("Authorization") authorization: String, @Path("item_id") item_id: String): Observable<String>

    @DELETE("/api/v2/items/{item_id}/stock")
    fun unStock(@Header("Authorization") authorization: String, @Path("item_id") item_id: String): Observable<String>
}