package com.ecobici.app.connection

import com.ecobici.app.classes.CONST
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

internal interface ApiCRUD {
    @GET
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun get(@Url url: String): Response<ResponseBody>
}

internal object ApiServices {
    val INSTANCE: ApiCRUD by lazy {
        Retrofit.Builder()
            .baseUrl(CONST.SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiCRUD::class.java)
    }
}