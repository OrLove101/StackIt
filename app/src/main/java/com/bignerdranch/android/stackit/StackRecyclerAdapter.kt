package com.bignerdranch.android.stackit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class StackRecyclerAdapter(private val mStackResponses: List<StackResponse.Item>): RecyclerView.Adapter<StackViewHolder>() {
    override fun getItemCount(): Int {
        if ( mStackResponses.isNotEmpty() ) {
            return mStackResponses.size
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val stackListRow = layoutInflater.inflate(R.layout.list_item_stack, parent, false)

        return StackViewHolder(stackListRow)
    }

    override fun onBindViewHolder(holder: StackViewHolder, position: Int) {
        holder.bind(mStackResponses[position])
    }
}

class StackViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private lateinit var mStackResponse: StackResponse.Item
    private var mTitleTextView: TextView
    private var mAnswerCountTextView: TextView
    private var mDateTextView: TextView
    private var mStackImageView: ImageView
    private var mProgressImageView: ProgressBar

    //TODO implement onClickListener interface

    init {
        mTitleTextView = view.findViewById(R.id.stack_title_list)
        mAnswerCountTextView = view.findViewById(R.id.stack_answers_list)
        mDateTextView = view.findViewById(R.id.stack_date_list)
        mStackImageView = view.findViewById(R.id.stack_image_list)
        mProgressImageView = view.findViewById(R.id.imageProgressBar)
    }

    fun bind(stackResponse: StackResponse.Item) {
        this.mStackResponse = stackResponse
        this.mTitleTextView.text = stackResponse.stackTitle
        this.mAnswerCountTextView.text = stackResponse.answerCount
        this.mDateTextView.text = stackResponse.humanDate
        Picasso.get().load(stackResponse.owner.photoUrl).into(this.mStackImageView, object : Callback {
            override fun onSuccess() {
                mProgressImageView.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                TODO("Not yet implemented")
            }
        })
    }
}