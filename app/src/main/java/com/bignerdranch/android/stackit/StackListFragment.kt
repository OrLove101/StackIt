package com.bignerdranch.android.stackit

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.NullPointerException

class StackListFragment : Fragment(), StackRequester.StackRequestResponse {
//    private var stackList = ArrayList<StackResponse.Item>()
    private lateinit var mStackRecyclerView: RecyclerView
    private lateinit var mStackAdapter: StackRecyclerAdapter
    private lateinit var stackRequester: StackRequester
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private val lastVisiblePosition: Int
        get() = mLinearLayoutManager.findLastVisibleItemPosition()

    companion object {
        fun newInstance(): StackListFragment {
            return StackListFragment()
        }
        private var stackList = ArrayList<StackResponse.Item>()
        private val TAG = "StackList"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_stack_list, container, false)

        mStackAdapter = StackRecyclerAdapter(stackList)
        mStackRecyclerView = view.findViewById(R.id.stack_recycler_view)
        mLinearLayoutManager =  LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        mStackRecyclerView.apply {
            this.layoutManager = mLinearLayoutManager
            this.adapter = mStackAdapter
            this.addItemDecoration(DividerItemDecoration(mStackRecyclerView.context, DividerItemDecoration.VERTICAL))
        }

        setRecyclerViewScrollListener()

        setRecyclerViewItemTouchListener()

        stackRequester = StackRequester(this)

        return view
    }

    private fun setRecyclerViewScrollListener() {
        mStackRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val totalItemCount = recyclerView.layoutManager!!.itemCount

                if ( !stackRequester.isLoadingdata && totalItemCount == lastVisiblePosition + 1 ) {
                    requestStack()
                }
            }
        })
    }

    private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                stackList.removeAt(position)
                mStackRecyclerView.adapter!!.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)

        itemTouchHelper.attachToRecyclerView(mStackRecyclerView)
    }

    override fun onStart() {
        super.onStart()
        if ( stackList.isEmpty() ) {
            requestStack()
        }
    }

    private fun requestStack() {
        stackRequester.getStack()
    }

    override fun receivedNewStack(newStackResponse: StackResponse) {
        this.activity?.runOnUiThread {
            var alreadyHas: Boolean = false
            var hasAtLeastOne: Boolean = false

            try {
                for ( element in newStackResponse.items ) {
                    for ( elem in stackList ) {
                        if ( elem.link == element.link ) {
                            alreadyHas = true
                            break
                        }
                    }
                    if ( !alreadyHas ) {
                        stackList.add(element)
                        mStackAdapter.notifyItemInserted(mStackAdapter.itemCount)

                        hasAtLeastOne = true
                    } else {
                        alreadyHas = false
                    }
                }
                if ( !hasAtLeastOne ) {
                    Toast.makeText(this.activity,
                        activity?.applicationContext?.getString(R.string.nothing_to_show_toast),
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: NullPointerException) {
                Toast.makeText(this.activity,
                    activity?.applicationContext?.getString(R.string.nothing_to_show_toast),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}