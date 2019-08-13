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
import com.example.movieapplication.data.adapter.TvShowAdapter
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.utils.Const
import com.example.movieapplication.viewmodel.tvshow.TvShowViewModel
import com.example.movieapplication.viewmodel.tvshow.TvShowViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject

class TVShowFragment : Fragment() {

    private lateinit var adapter: TvShowAdapter

    @Inject
    lateinit var tvShowViewModel: TvShowViewModel

    @Inject
    lateinit var tvShowViewModelFactory: TvShowViewModelFactory

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
        tvShowViewModel = ViewModelProviders.of(this, tvShowViewModelFactory).get(TvShowViewModel::class.java)
        tvShowViewModel.tvsResult().observe(this, tvs)

        if (tvShowViewModel.isError()) {
            pbMovie.visibility = View.GONE
            Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
        }
    }

    private val error = Observer<Boolean> { error ->
        if (error != null) {
            if (error) {
                pbMovie.visibility = View.GONE
                Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        pbMovie.visibility = View.GONE
        Toast.makeText(context, context?.resources?.getString(R.string.msg_fail_data), Toast.LENGTH_SHORT).show()
    }

    private val tvs = Observer<HashMap<String, Any>> { map ->
        if (map != null) {
            val list: List<TvShow> = map[Const.PARCEL_KEY_TV] as List<TvShow>
            val bitmap: List<Bitmap> = map[Const.PARCEL_KEY_BITMAP] as List<Bitmap>
            adapter = TvShowAdapter(list, bitmap)
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