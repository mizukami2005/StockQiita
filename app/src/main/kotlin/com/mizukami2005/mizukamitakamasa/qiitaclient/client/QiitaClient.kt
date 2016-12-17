package com.mizukami2005.mizukamitakamasa.qiitaclient.client

import android.net.Uri
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.ResponseToken
import com.mizukami2005.mizukamitakamasa.qiitaclient.model.User
import retrofit2.http.*
import rx.Observable
import java.util.*

/**
 * Created by mizukamitakamasa on 2016/10/02.
 */

interface QiitaClient {
    @Headers("Content-Type: application/json")
    @POST("/api/v2/access_tokens")
    fun access(@Body map: HashMap<String, String>): Observable<ResponseToken>

    @GET("/api/v2/authenticated_user")
    fun getUser(@Header("Authorization") authorization: String) : Observable<User>
}