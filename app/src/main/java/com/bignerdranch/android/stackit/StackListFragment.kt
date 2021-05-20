package com.bignerdranch.android.stackit

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.lang.NullPointerException

class StackListFragment : Fragment(), StackRequester.StackRequestResponse {
//    private var stackList = ArrayList<StackResponse.Item>()
    private lateinit var mStackRecyclerView: RecyclerView
    private lateinit var mStackAdapter: StackRecyclerAdapter
    private lateinit var stackRequester: StackRequester
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var mQuery: String? = null
    private val lastVisiblePosition: Int
        get() = mLinearLayoutManager.findLastVisibleItemPosition()

    companion object {
        private var stackList = ArrayList<StackResponse.Item>()
        private val TAG = "StackList"
        private val STACK_LIST_QUERY = "stackListQuery"
        private var isRecentLoaded: Boolean = true

        fun newInstance(): StackListFragment {
            return StackListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if ( savedInstanceState != null ) {
            mQuery = savedInstanceState.getString(STACK_LIST_QUERY)
            if ( mQuery != null ) {
                activity?.title = mQuery
            }
        }
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_stack_list, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                isRecentLoaded = false
                mQuery = query
                stackList.clear()
                stackRequester.searchStacks(query.toString())
                if ( isAdded ) {
                    mStackAdapter.notifyDataSetChanged()
                }
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.isIconified = true
                searchItem.collapseActionView()
                activity!!.title = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_update -> nowLoadRecent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun nowLoadRecent() {
        isRecentLoaded = true
        stackList.clear()
        stackRequester.getRecentStacks()
        if ( isAdded ) {
            mStackAdapter.notifyDataSetChanged()
        }
        activity?.title = activity?.applicationContext?.getString(R.string.app_name)
        mQuery = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STACK_LIST_QUERY, mQuery)
    }

    private fun setRecyclerViewScrollListener() {
        mStackRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val totalItemCount = recyclerView.layoutManager!!.itemCount

                if ( !stackRequester.isLoadingdata && totalItemCount == lastVisiblePosition + 1 ) {
                    if (isRecentLoaded) {
                        requestStack()
                    } else {
                        stackRequester.searchStacks(mQuery.toString())
                    }
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
        stackRequester.getRecentStacks()
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
                    activity?.applicationContext?.getString(R.string.too_many_requests_toast),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}