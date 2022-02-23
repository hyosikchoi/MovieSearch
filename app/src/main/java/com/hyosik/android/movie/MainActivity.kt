package com.hyosik.android.movie

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hyosik.android.movie.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        selectFragment(HomeFragment() , HomeFragment.TAG)
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> {
                    selectFragment(HomeFragment(),HomeFragment.TAG)
                    true
                }

                R.id.menu_mypage -> {
                    selectFragment(MyPageFragment(),MyPageFragment.TAG)
                    true
                }

                else -> false
            }
        }
    }

    private fun selectFragment(fragment : Fragment , tag : String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run { supportFragmentManager.beginTransaction().add(R.id.fragmentContainer , fragment , tag).commitAllowingStateLoss() }
    }

}