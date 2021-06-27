package com.bignerdranch.android.stackit

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class SingleFragmentActivity : AppCompatActivity() {
    protected abstract fun createFragment(): Fragment

    @LayoutRes
    protected fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        if ( savedInstanceState == null ) {
            val fragment = createFragment()
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, fragment)
                commit()
            }
        }
    }
}