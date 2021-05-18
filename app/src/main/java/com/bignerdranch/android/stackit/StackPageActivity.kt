package com.bignerdranch.android.stackit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class StackPageActivity : SingleFragmentActivity() {
    companion object {
        private val EXTRA_LINK = "extraLink"

        fun newIntent(context: Context, stackPageLink: String): Intent {
            val intent = Intent(context, StackPageActivity::class.java)

            intent.putExtra(EXTRA_LINK, stackPageLink)

            return intent
        }
    }

    override fun createFragment(): Fragment {
        return StackPageFragment.newInstance(intent.getStringExtra(EXTRA_LINK).toString())
    }
}