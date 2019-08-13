package com.example.movieapplication.data.adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.movieapplication.R
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.LanguageManager
import com.example.movieapplication.view.activity.MovieDetailActivity
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModel

class MovieFavoriteAdapter(
    private var movies: List<Movie>,
    private var bitmaps: List<Bitmap>,
    private val viewModel: MovieFavoriteViewModel,
    private val builder: AlertDialog.Builder
) :

    RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieFavoriteViewHolder {
        val itemRow: View = LayoutInflater.from(p0.context).inflate(R.layout.item_movie_favorite, p0, false)
        return MovieFavoriteViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieFavoriteViewHolder, position: Int) {
        holder.tvTitle.text = movies[position].title
        holder.tvLang.text = movies[position].language?.let { LanguageManager.getLanguage(holder.itemView.context, it) }
        holder.tvRate.text = movies[position].rating.toString()
        holder.tvDesc.text = movies[position].overview
        holder.ivIcon.setImageBitmap(bitmaps[position])

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            viewModel.deleteMovie(movies[position])
            notifyDataSetChanged()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            builder.setCancelable(true)
        }

        holder.ivDelete.setOnClickListener { view ->
            builder.show()
        }

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, MovieDetailActivity::class.java)
            intent.putExtra(Const.PARCEL_KEY_FAVORITE, true)
            intent.putExtra(Const.PARCEL_KEY_MOVIE, movies[position])
            intent.putExtra(Const.PARCEL_KEY_BITMAP, bitmaps[position])
            view.context.startActivity(intent)
        }
    }

    class MovieFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvListFavTitle)
        val tvRate: TextView = itemView.findViewById(R.id.tvListFavRate)
        val tvLang: TextView = itemView.findViewById(R.id.tvListFavLang)
        val tvDesc: TextView = itemView.findViewById(R.id.tvListFavDesc)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivListFavPict)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivListFavDelete)
    }
}