package com.bignerdranch.android.stackit

import android.net.Uri
import androidx.fragment.app.Fragment
import okhttp3.*
import java.io.IOException

class StackRequester(listeningFragment: Fragment) {
    private val client: OkHttpClient = OkHttpClient()
    private val responseListener: StackRequestResponse = listeningFragment as StackRequestResponse
    var isLoadingdata: Boolean = false
        private set
    var resultUpdated: Boolean = false

    fun getRecentStacks() {
        if ( resultUpdated ) {
            currentPage = 1
            resultUpdated = false
        }

        val urlRequest = Uri.Builder().scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_1)
            .appendPath(URL_QUESTIONS_PATH)
            .appendQueryParameter(URL_QUERY_PARAM_PAGE, currentPage.toString())
            .appendQueryParameter(URL_QUERY_PARAM_PAGE_SIZE, "20")
            .appendQueryParameter(URL_QUERY_PARAM_ORDER, "desc")
            .appendQueryParameter(URL_QUERY_PARAM_SORT, "activity")
            .appendQueryParameter(URL_QUERY_PARAM_SITE, "stackoverflow")
            .build().toString()

        getStack(urlRequest)
        currentPage += 1
    }

    fun searchStacks(query: String) {
        if ( lastQuery != query ) {
            currentPage = 1
        }

        val urlRequest = Uri.Builder().scheme(URL_SCHEME)
            .authority(URL_AUTHORITY)
            .appendPath(URL_PATH_1)
            .appendPath(URL_SEARCH_PATH)
            .appendQueryParameter(URL_QUERY_PARAM_PAGE, currentPage.toString())
            .appendQueryParameter(URL_QUERY_PARAM_PAGE_SIZE, "20")
            .appendQueryParameter(URL_QUERY_PARAM_ORDER, "desc")
            .appendQueryParameter(URL_QUERY_PARAM_SORT, "activity")
            .appendQueryParameter(URL_QUERY_PARAM_TITLE, query)
            .appendQueryParameter(URL_QUERY_PARAM_SITE, "stackoverflow")
            .build().toString()

        getStack(urlRequest)
        currentPage += 1
        lastQuery = query
    }

    private fun getStack(url: String) {
        val request = Request.Builder().url(url).build()

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

    interface StackRequestResponse {
        fun receivedNewStack(newStackResponse: StackResponse)
    }

    companion object {
        private const val URL_SCHEME = "https"
        private const val URL_AUTHORITY = "api.stackexchange.com"
        private const val URL_PATH_1 = "2.2"
        private const val URL_QUESTIONS_PATH = "questions"
        private const val URL_QUERY_PARAM_PAGE = "page"
        private const val URL_QUERY_PARAM_PAGE_SIZE = "pagesize"
        private const val URL_QUERY_PARAM_ORDER = "order"
        private const val URL_QUERY_PARAM_SORT = "sort"
        private const val URL_QUERY_PARAM_SITE = "site"
        private const val URL_SEARCH_PATH = "search"
        private const val URL_QUERY_PARAM_TITLE = "intitle"
        private const val TAG = "StackRequest"

        private var currentPage = 1
        private var lastQuery: String? = null
    }
}