package com.example.movieapplication.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.movieapplication.R
import com.example.movieapplication.data.adapter.PagerAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment() {
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.search)?.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter = context?.let { fragmentManager?.let { it1 -> PagerAdapter(it, it1) } }
        vpFavorite.adapter = fragmentAdapter
        tlFavorite.setupWithViewPager(vpFavorite)
    }
}
