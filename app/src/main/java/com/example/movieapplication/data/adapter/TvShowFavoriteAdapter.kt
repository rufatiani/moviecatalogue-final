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
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.Preferences
import com.example.movieapplication.view.activity.MovieDetailActivity
import com.example.movieapplication.viewmodel.tvshow.TvShowFavoriteViewModel

class TvShowFavoriteAdapter(
    private var tvShows: List<TvShow>,
    private var bitmaps: List<Bitmap>,
    private val viewModel: TvShowFavoriteViewModel,
    private val builder: AlertDialog.Builder
) :
    RecyclerView.Adapter<TvShowFavoriteAdapter.TvShowFavoriteViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TvShowFavoriteViewHolder {
        val itemRow: View = LayoutInflater.from(p0.context).inflate(R.layout.item_movie_favorite, p0, false)
        return TvShowFavoriteViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return tvShows.size
    }

    override fun onBindViewHolder(holder: TvShowFavoriteViewHolder, position: Int) {
        holder.tvTitle.text = tvShows[position].name
        holder.tvLang.text =
            tvShows[position].language?.let { Preferences.getLanguage(holder.itemView.context, it) }
        holder.tvRate.text = tvShows[position].rating.toString()
        holder.tvDesc.text = tvShows[position].overview
        holder.ivIcon.setImageBitmap(bitmaps[position])

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            viewModel.deleteTvShow(tvShows[position])
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
            intent.putExtra(Const.PARCEL_KEY_TV, tvShows[position])
            intent.putExtra(Const.PARCEL_KEY_BITMAP, bitmaps[position])
            view.context.startActivity(intent)
        }
    }

    class TvShowFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvListFavTitle)
        val tvRate: TextView = itemView.findViewById(R.id.tvListFavRate)
        val tvLang: TextView = itemView.findViewById(R.id.tvListFavLang)
        val tvDesc: TextView = itemView.findViewById(R.id.tvListFavDesc)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivListFavPict)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivListFavDelete)
    }
}