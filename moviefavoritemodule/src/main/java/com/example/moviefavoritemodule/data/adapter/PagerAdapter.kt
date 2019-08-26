package com.example.moviefavoritemodule.data.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.moviefavoritemodule.R
import com.example.moviefavoritemodule.view.fragment.MovieFragment
import com.example.moviefavoritemodule.view.fragment.TvShowFragment

class PagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = listOf(
        MovieFragment(),
        TvShowFragment()
    )

    override fun getItem(potition: Int): Fragment {
        return fragments[potition] as Fragment
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