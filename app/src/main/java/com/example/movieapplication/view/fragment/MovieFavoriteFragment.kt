package com.example.movieapplication.view.fragment

import android.app.AlertDialog
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
import com.example.movieapplication.data.adapter.MovieFavoriteAdapter
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.utils.Const
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModel
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_favorite.*
import javax.inject.Inject

class MovieFavoriteFragment : Fragment() {
    private lateinit var adapter: MovieFavoriteAdapter

    private lateinit var builder: AlertDialog.Builder

    @Inject
    lateinit var movieViewFavoriteModel: MovieFavoriteViewModel

    @Inject
    lateinit var movieFavoriteViewModelFactory: MovieFavoriteViewModelFactory

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager : SearchManager? = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchManager != null){
            val searchView : SearchView = (menu?.findItem(R.id.search))?.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.isEmpty()){
                        pbMovieFavorite.visibility = View.VISIBLE
                        movieViewFavoriteModel.loadMovies()
                        movieViewFavoriteModel.moviesResult().observe(viewLifecycleOwner, movies)
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        pbMovieFavorite.visibility = View.VISIBLE
                        movieViewFavoriteModel.searchMovies(query)
                        movieViewFavoriteModel.moviesResult().observe(viewLifecycleOwner, movies)
                    }
                    return true
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(R.string.var_msg_title))
        builder.setMessage(resources.getString(R.string.var_msg))

        initializeRecycler()

        pbMovieFavorite.visibility = View.VISIBLE
        movieViewFavoriteModel =
            ViewModelProviders.of(this, movieFavoriteViewModelFactory).get(MovieFavoriteViewModel::class.java)

        movieViewFavoriteModel.moviesResult().observe(viewLifecycleOwner, movies)

        if (movieViewFavoriteModel.isError()) {
            pbMovieFavorite.visibility = View.GONE
            Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
        }

        builder.setOnDismissListener {
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
            val list: List<Movie>? = map[Const.PARCEL_KEY_MOVIE] as List<Movie>
            val bitmap: List<Bitmap>? = map[Const.PARCEL_KEY_BITMAP] as List<Bitmap>

            if (list != null && bitmap != null) {
                adapter = MovieFavoriteAdapter(
                    list,
                    bitmap,
                    movieViewFavoriteModel,
                    builder
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
