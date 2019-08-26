package com.example.moviefavoritemodule.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.moviefavoritemodule.R
import com.example.moviefavoritemodule.data.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = PagerAdapter(this, supportFragmentManager)
        vpFavorite.adapter = fragmentAdapter
        tlFavorite.setupWithViewPager(vpFavorite)
    }


}
