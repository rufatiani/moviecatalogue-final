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
import com.example.moviefavoritemodule.data.model.TvShow
import com.example.moviefavoritemodule.util.Const
import com.example.moviefavoritemodule.view.activity.MovieDetailActivity

class TvShowAdapter(private var tvShows: List<TvShow>, private var bitmaps: List<Bitmap>) :
    RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TvShowViewHolder {
        val itemRow: View = LayoutInflater.from(p0.context).inflate(R.layout.item_movie, p0, false)
        return TvShowViewHolder(itemRow)
    }

    override fun getItemCount(): Int {
        return tvShows.size
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.tvTitle.text = tvShows[position].name
        holder.tvLang.text = tvShows[position].language
        holder.tvRate.text = tvShows[position].rating.toString()
        holder.tvDesc.text = tvShows[position].overview
        holder.ivIcon.setImageBitmap(bitmaps[position])

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, MovieDetailActivity::class.java)
            intent.putExtra(Const.PARCEL_KEY_FAVORITE, false)
            intent.putExtra(Const.PARCEL_KEY_TV, tvShows[position])
            intent.putExtra(Const.PARCEL_KEY_BITMAP, bitmaps[position])
            view.context.startActivity(intent)
        }
    }

    class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvListTitle)
        val tvRate: TextView = itemView.findViewById(R.id.tvListRate)
        val tvLang: TextView = itemView.findViewById(R.id.tvListLang)
        val tvDesc: TextView = itemView.findViewById(R.id.tvListDesc)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivListPict)
    }
}