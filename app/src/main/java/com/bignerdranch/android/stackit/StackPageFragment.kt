package com.bignerdranch.android.stackit

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

class StackPageFragment : Fragment() {
    private lateinit var mStackLink: String
    private lateinit var mStackWebView: WebView
    private lateinit var mStackWebProgress: ProgressBar

    companion object {
        private val EXTRA_LINK = "extraLink"
        private val WEB_VIEW_STATE_KEY = "webViewState"

        fun newInstance(link: String): StackPageFragment {
            val args: Bundle = Bundle()
            val fragment = StackPageFragment()

            args.putString(EXTRA_LINK, link)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mStackLink = arguments!!.getString(EXTRA_LINK).toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_stack_webview, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_share -> startShareIntent()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_stack_web_page, container, false)

        mStackWebView = view.findViewById(R.id.stack_web_view)
        mStackWebProgress = view.findViewById(R.id.stack_web_progress)

        mStackWebView.apply {
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            this.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            this.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mStackWebProgress.visibility = View.GONE
                    mStackWebView.visibility = View.VISIBLE
                }
            }
            this.settings.apply {
                this.supportZoom()
                this.builtInZoomControls = true
                this.displayZoomControls = false
            }
            this.loadUrl(mStackLink)
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val webViewBundle = Bundle()

        mStackWebView.saveState(webViewBundle)
        outState.putBundle(WEB_VIEW_STATE_KEY, webViewBundle)
    }

    private fun startShareIntent() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Look at this:\n $mStackLink")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent,
            activity?.applicationContext?.getString(R.string.web_view_chooser_title))

        startActivity(shareIntent)
    }
}