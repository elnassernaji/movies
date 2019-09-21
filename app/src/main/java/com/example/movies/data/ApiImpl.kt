package com.example.movies.data

import android.content.Context
import com.example.movies.R
import com.example.movies.model.Request
import com.example.movies.model.Response
import io.reactivex.Observable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.IOException

class ApiImpl(private val context: Context, private val httpClient: OkHttpClient): Api<Request, Response> {

    override fun get(request: Request): Observable<Response> {
        val rb = HttpUrl.parse(request.url)?.newBuilder()

        rb?.addQueryParameter("api_key", context.getString(R.string.api_key))

        request.params?.apply {
            keys.forEach {
                rb?.addQueryParameter(it, this[it])
            }
        }

        return Observable.create { subs ->
            val call = httpClient.newCall(okhttp3.Request.Builder().url(rb?.build()!!).build())
            call.enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    subs.onError(e)
                }

                override fun onResponse(call: Call?, response: okhttp3.Response?) {
                    val responseString = response?.body()?.string()
                    if (response!!.isSuccessful) {
                        subs.onNext(Response(responseString))
                    } else  {
                        val responseJson = JSONObject(responseString)
                        if(responseJson.has("message")) {
                            subs.onError(Throwable(responseJson.optString("message")))
                        } else subs.onError(Throwable("Response failure: " + response.message()))
                    }
                }
            })
        }
    }

}