package com.ecobici.app.connection

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import kotlin.Exception


class ApiRepository {
    private suspend fun getResponse(call: suspend ()  -> Response<ResponseBody>, callback: Callback): Callback {
        try{
            if (call().isSuccessful){
                callback.success(JSONObject(call().body()!!.string()))
            }else{
                callback.error(Exception("Ha ocurrido un error en el servicio"))
            }
        }catch (e: Exception){
            callback.error(Exception("Ha ocurrido un error al procesar la informacion"))
        }
        return callback
    }


    suspend fun get(path: String, callback: Callback) = getResponse({ ApiServices.INSTANCE.get(path) }, callback)


    interface Callback{
        fun success(response: JSONObject)
        fun error(error: Exception)
    }
}