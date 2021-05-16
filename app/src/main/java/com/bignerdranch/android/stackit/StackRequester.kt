package com.bignerdranch.android.stackit

import android.net.Uri
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class StackRequester(listeningFragment: Fragment) {
    interface StackRequestResponse {
        fun receivedNewStack(newStackResponse: StackResponse)
    }

    private val client: OkHttpClient
    private val responseListener: StackRequestResponse
    var isLoadingdata: Boolean = false
        private set

    init {
        responseListener = listeningFragment as StackRequestResponse
        client = OkHttpClient()
    }

    fun getStack() {
        val urlRequest = Uri.Builder().scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_1)
            .appendPath(URL_PATH_2)
            .appendQueryParameter(URL_QUERY_PARAM_PAGE_SIZE, "8")
            .appendQueryParameter(URL_QUERY_PARAM_ORDER, "desc")
            .appendQueryParameter(URL_QUERY_PARAM_SORT, "activity")
            .appendQueryParameter(URL_QUERY_PARAM_SITE, "stackoverflow")
            .build().toString()
        val request = Request.Builder().url(urlRequest).build()

        isLoadingdata = true

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                isLoadingdata = false
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                val receivedStack = StackResponse.parseJson(body)

                responseListener.receivedNewStack(receivedStack)
                isLoadingdata = false
//----------------------------------WITHOUT GSON----------------------------------------------------
//                val stackJSON = JSONObject(body).getJSONArray("items").getJSONObject(0)
//                val receivedStack = StackResponse(stackJSON)
//
//                responseListener.receivedNewStack(receivedStack)
//--------------------------------------------------------------------------------------------------
            }
        })

    }

    companion object {
        private val URL_SCHEME = "https"
        private val URL_AUTHORITY = "api.stackexchange.com"
        private val URL_PATH_1 = "2.2"
        private val URL_PATH_2 = "questions"
        private val URL_QUERY_PARAM_PAGE_SIZE = "pagesize"
        private val URL_QUERY_PARAM_ORDER = "order"
        private val URL_QUERY_PARAM_SORT = "sort"
        private val URL_QUERY_PARAM_SITE = "site"
        private val STACK_REQUEST_DEBUG = "StackRequest"
    }
}