package com.example.movieapplication.view.fragment

import android.app.AlertDialog
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.example.movieapplication.R
import com.example.movieapplication.data.adapter.TvShowFavoriteAdapter
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.utils.Const
import com.example.movieapplication.viewmodel.tvshow.TvShowFavoriteViewModel
import com.example.movieapplication.viewmodel.tvshow.TvShowFavoriteViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_favorite.*
import javax.inject.Inject

class TvShowFavoriteFragment : Fragment() {
    private lateinit var adapter: TvShowFavoriteAdapter

    @Inject
    lateinit var tvShowFavoriteViewModel: TvShowFavoriteViewModel

    @Inject
    lateinit var tvShowFavoriteViewModelFactory: TvShowFavoriteViewModelFactory

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecycler()

        pbMovieFavorite.visibility = View.VISIBLE
        tvShowFavoriteViewModel =
            ViewModelProviders.of(this, tvShowFavoriteViewModelFactory).get(TvShowFavoriteViewModel::class.java)

        tvShowFavoriteViewModel.tvsResult().observe(viewLifecycleOwner, tvs)

        if (tvShowFavoriteViewModel.isError()) {
            pbMovieFavorite.visibility = View.GONE
            Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
        }

        activity?.baseContext?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver,
                IntentFilter("refresh-tv")
            )
        }
    }

    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            tvShowFavoriteViewModel.loadTvs()
            tvShowFavoriteViewModel.tvsResult().observe(viewLifecycleOwner, tvs)
        }
    }

    override fun onResume() {
        super.onResume()
        tvShowFavoriteViewModel.loadTvs()
        tvShowFavoriteViewModel.tvsResult().observe(viewLifecycleOwner, tvs)
    }

    private val tvs = Observer<HashMap<String, Any>> { map ->
        if (map != null) {
            val list: ArrayList<TvShow>? = map[Const.PARCEL_KEY_TV] as ArrayList<TvShow>
            val bitmap: ArrayList<Bitmap>? = map[Const.PARCEL_KEY_BITMAP] as ArrayList<Bitmap>

            if (list != null && bitmap != null) {
                adapter = TvShowFavoriteAdapter(
                    list,
                    bitmap,
                    tvShowFavoriteViewModel
                )
                adapter.notifyDataSetChanged()
                rlMoviesFavorite.adapter = adapter
            }
        }

        pbMovieFavorite.visibility = View.GONE
    }

    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(activity, 1)
        gridLayoutManager.orientation = RecyclerView.VERTICAL
        rlMoviesFavorite.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
        }
    }
}