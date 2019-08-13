package com.example.movieapplication.view.fragment

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var builder: AlertDialog.Builder

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

        builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(R.string.var_msg_title))
        builder.setMessage(resources.getString(R.string.var_msg))

        initializeRecycler()

        pbMovieFavorite.visibility = View.VISIBLE
        tvShowFavoriteViewModel =
            ViewModelProviders.of(this, tvShowFavoriteViewModelFactory).get(TvShowFavoriteViewModel::class.java)

        tvShowFavoriteViewModel.tvsResult().observe(this, tvs)

        if (tvShowFavoriteViewModel.isError()) {
            pbMovieFavorite.visibility = View.GONE
            Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
        }

        builder.setOnDismissListener {
            tvShowFavoriteViewModel.loadTvs()
            tvShowFavoriteViewModel.tvsResult().observe(this, tvs)
        }
    }

    override fun onResume() {
        super.onResume()
        tvShowFavoriteViewModel.loadTvs()
        tvShowFavoriteViewModel.tvsResult().observe(this, tvs)
    }

    private val tvs = Observer<HashMap<String, Any>> { map ->
        if (map != null) {
            val list: List<TvShow>? = map[Const.PARCEL_KEY_TV] as List<TvShow>
            val bitmap: List<Bitmap>? = map[Const.PARCEL_KEY_BITMAP] as List<Bitmap>

            if (list != null && bitmap != null) {
                adapter = TvShowFavoriteAdapter(
                    list,
                    bitmap,
                    tvShowFavoriteViewModel,
                    builder
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