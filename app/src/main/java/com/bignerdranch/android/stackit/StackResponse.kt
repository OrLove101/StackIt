package com.bignerdranch.android.stackit

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class StackResponse(val items: List<Item>) {
    companion object {
        fun parseJson(response: String): StackResponse {
            val gson = GsonBuilder().create()

            return gson.fromJson(response, StackResponse::class.java)
        }
    }

    inner class Item(val owner: Owner,
               @SerializedName("creation_date")
               private val stackDate: String,
               @SerializedName("title")
               val stackTitle: String,
               @SerializedName("answer_count")
               val answerCount: String,
                     val link: String
    ) {
        val humanDate: String
            get() = toHumanDate()

        private fun toHumanDate(): String {
            val dateFormat = SimpleDateFormat("yyyy.MM.dd")
            val time = Date(this.stackDate.toLong() * 1000)

            return dateFormat.format(time)
        }
    }

    inner class Owner(
        @SerializedName("profile_image")
        val photoUrl: String
    )
}



//-----------------------------------WITHOUT GSON---------------------------------------------------
//class StackResponse(stackJSON: JSONObject) : Serializable {
//    private lateinit var stackDate: String
//    lateinit var humanDate: String
//        private set
//    lateinit var stackTitle: String
//        private set
//    lateinit var photoUrl: String
//        private set
//    lateinit var answerCount: String
//        private set
//
//    init {
//       try {
//           stackDate = stackJSON.getString(STACK_DATE)
//           humanDate = toHumanDate()
//           stackTitle = stackJSON.getString(STACK_TITLE)
//           answerCount = stackJSON.getString(STACK_ANSWER_COUNT)
//           photoUrl = stackJSON.getJSONObject("owner").getString(STACK_PHOTO_URL)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun toHumanDate(): String {
//        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
//        val time = Date(this.stackDate.toLong() * 1000)
//
//        return dateFormat.format(time)
//    }
//
//    companion object {
//        private val STACK_DATE = "creation_date"
//        private val STACK_TITLE = "title"
//        private val STACK_PHOTO_URL = "profile_image"
//        private val STACK_ANSWER_COUNT = "answer_count"
//    }
//}
//--------------------------------------------------------------------------------------------------