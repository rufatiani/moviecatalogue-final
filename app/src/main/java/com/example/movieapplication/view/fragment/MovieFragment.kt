package com.example.movieapplication.view.fragment

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
import com.example.movieapplication.data.adapter.MovieAdapter
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.utils.Const
import com.example.movieapplication.viewmodel.movie.MovieViewModel
import com.example.movieapplication.viewmodel.movie.MovieViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject


class MovieFragment : Fragment() {

    private lateinit var adapter: MovieAdapter

    @Inject
    lateinit var movieViewModel: MovieViewModel

    @Inject
    lateinit var movieViewModelFactory: MovieViewModelFactory

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecycler()

        pbMovie.visibility = View.VISIBLE
        movieViewModel = ViewModelProviders.of(this, movieViewModelFactory).get(MovieViewModel::class.java)

        movieViewModel.moviesResult().observe(this, movies)

        if (movieViewModel.isError()) {
            pbMovie.visibility = View.GONE
            Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
        }
    }

    private val movies = Observer<HashMap<String, Any>> { map ->
        if (map != null) {
            val list: List<Movie> = map[Const.PARCEL_KEY_MOVIE] as List<Movie>
            val bitmap: List<Bitmap> = map[Const.PARCEL_KEY_BITMAP] as List<Bitmap>
            adapter = MovieAdapter(list, bitmap)
            rlMovies.adapter = adapter
        }

        pbMovie.visibility = View.GONE
    }

    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(activity, 1)
        gridLayoutManager.orientation = RecyclerView.VERTICAL
        rlMovies.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
        }
    }
}