package com.bignerdranch.android.stackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return StackListFragment.newInstance()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { (supportFragmentManager.fragments.last() as StackListFragment).onNewIntent(it) }
    }
}