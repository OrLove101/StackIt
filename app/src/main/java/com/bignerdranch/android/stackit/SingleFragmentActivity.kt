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

//        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//        if ( fragment == null ) { //fragment will be null if in fragment_container there`s nothing (books version)

//IF WE DONT PERFORM THIS CHECK, THEN WHEN ACTIVITY THAT ALREADY HAVE A FRAGMENT DESTROYED FRAGMENT STILL PART OF IT
//SO ONCREATE CALLED AGAIN AND NEW FRAGMENT CREATES AND OVERLAP PREVIOUS

        if ( savedInstanceState == null ) {//This is a way to see if it’s an initial open of the screen. (guide version)
            val fragment = createFragment()
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, fragment)
                commit()
            }
        }

//        }
    }
}