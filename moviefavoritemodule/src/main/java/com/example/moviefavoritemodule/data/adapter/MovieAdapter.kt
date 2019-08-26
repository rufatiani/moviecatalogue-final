package com.example.moviefavoritemodule.data.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.moviefavoritemodule.R
import com.example.moviefavoritemodule.data.model.Movie
import com.example.moviefavoritemodule.util.Const
import com.example.moviefavoritemodule.view.activity.MovieDetailActivity

class MovieAdapter(private var movies: List<Movie>, private var bitmaps: List<Bitmap>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val itemRow: View = LayoutInflater.from(p0.context).inflate(R.layout.item_movie, p0, false)
        return MovieViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.tvTitle.text = movies[position].title
        holder.tvLang.text = movies[position].language
        holder.tvRate.text = movies[position].rating.toString()
        holder.tvDesc.text = movies[position].overview
        holder.ivIcon.setImageBitmap(bitmaps[position])

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, MovieDetailActivity::class.java)
            intent.putExtra(Const.PARCEL_KEY_FAVORITE, false)
            intent.putExtra(Const.PARCEL_KEY_MOVIE, movies[position])
            intent.putExtra(Const.PARCEL_KEY_BITMAP, bitmaps[position])
            view.context.startActivity(intent)
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvListTitle)
        val tvRate: TextView = itemView.findViewById(R.id.tvListRate)
        val tvLang: TextView = itemView.findViewById(R.id.tvListLang)
        val tvDesc: TextView = itemView.findViewById(R.id.tvListDesc)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivListPict)
    }
}