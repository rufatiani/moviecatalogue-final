package com.example.movieapplication.view.fragment

import android.app.AlertDialog
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.example.movieapplication.data.adapter.MovieFavoriteAdapter
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.utils.Const
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModel
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_favorite.*
import javax.inject.Inject
import android.content.IntentFilter
import com.example.movieapplication.R


class MovieFavoriteFragment : Fragment() {
    private lateinit var adapter: MovieFavoriteAdapter

    @Inject
    lateinit var movieViewFavoriteModel: MovieFavoriteViewModel

    @Inject
    lateinit var movieFavoriteViewModelFactory: MovieFavoriteViewModelFactory

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
        movieViewFavoriteModel =
            ViewModelProviders.of(this, movieFavoriteViewModelFactory).get(MovieFavoriteViewModel::class.java)

        movieViewFavoriteModel.moviesResult().observe(viewLifecycleOwner, movies)

        if (movieViewFavoriteModel.isError()) {
            pbMovieFavorite.visibility = View.GONE
            Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
        }

        activity?.baseContext?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver,
                IntentFilter("refresh-movie"))
        }
    }

    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            movieViewFavoriteModel.loadMovies()
            movieViewFavoriteModel.moviesResult().observe(viewLifecycleOwner, movies)
        }
    }

    override fun onResume() {
        super.onResume()
        movieViewFavoriteModel.loadMovies()
        movieViewFavoriteModel.moviesResult().observe(viewLifecycleOwner, movies)
    }

    private val movies = Observer<HashMap<String, Any>> { map ->
        if (map != null) {
            val list: ArrayList<Movie>? = map[Const.PARCEL_KEY_MOVIE] as ArrayList<Movie>
            val bitmap: ArrayList<Bitmap>? = map[Const.PARCEL_KEY_BITMAP] as ArrayList<Bitmap>

            if (list != null && bitmap != null) {
                adapter = MovieFavoriteAdapter(
                    list,
                    bitmap,
                    movieViewFavoriteModel
                )

                rlMoviesFavorite.adapter = adapter
                adapter.notifyDataSetChanged()
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
