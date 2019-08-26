package com.example.movieapplication.view.fragment

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager: SearchManager? = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchManager != null) {
            val searchView: SearchView = (menu?.findItem(R.id.search))?.actionView as SearchView
            if (!searchView.isIconified) {
                searchView.isIconified = true
            }
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.isEmpty()) {
                        pbMovie.visibility = View.VISIBLE
                        movieViewModel.loadMovies()
                        movieViewModel.moviesResult().observe(viewLifecycleOwner, movies)
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        pbMovie.visibility = View.VISIBLE
                        movieViewModel.searchMovies(query)
                        movieViewModel.moviesResult().observe(viewLifecycleOwner, movies)
                    }
                    return true
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecycler()

        pbMovie.visibility = View.VISIBLE
        movieViewModel = ViewModelProviders.of(this, movieViewModelFactory).get(MovieViewModel::class.java)

        movieViewModel.moviesResult().observe(viewLifecycleOwner, movies)

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