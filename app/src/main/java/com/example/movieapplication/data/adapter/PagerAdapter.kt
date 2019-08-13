package com.example.movieapplication.data.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.movieapplication.R
import com.example.movieapplication.view.fragment.MovieFavoriteFragment
import com.example.movieapplication.view.fragment.TvShowFavoriteFragment

class PagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = listOf(
        MovieFavoriteFragment(),
        TvShowFavoriteFragment()
    )

    override fun getItem(potition: Int): Fragment {
        return fragments[potition]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.title_movie)
            else -> return context.resources.getString(R.string.title_tvshow)
        }
    }
}