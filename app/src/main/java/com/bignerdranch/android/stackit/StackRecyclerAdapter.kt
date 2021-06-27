package com.bignerdranch.android.stackit

import android.util.Log
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

class StackRecyclerAdapter(private val mStackResponses: List<StackResponse.Item>):
    RecyclerView.Adapter<StackViewHolder>() {
    override fun getItemCount(): Int {
        return mStackResponses.size
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

class StackViewHolder(private val view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
    private lateinit var mStackResponse: StackResponse.Item
    private var mTitleTextView: TextView = view.findViewById(R.id.stack_title_list)
    private var mAnswerCountTextView: TextView = view.findViewById(R.id.stack_answers_list)
    private var mDateTextView: TextView = view.findViewById(R.id.stack_date_list)
    private var mStackImageView: ImageView = view.findViewById(R.id.stack_image_list)
    private var mProgressImageView: ProgressBar = view.findViewById(R.id.imageProgressBar)

    init {
        view.setOnClickListener(this)
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
                //TODO("Not yet implemented")
            }
        })
    }

    override fun onClick(v: View?) {
        val context = itemView.context
        val showStackIntent = StackPageActivity.newIntent(context, mStackResponse.link)

        context.startActivity(showStackIntent)
    }

    companion object {
        private val TAG = "StackRecycler"
    }
}