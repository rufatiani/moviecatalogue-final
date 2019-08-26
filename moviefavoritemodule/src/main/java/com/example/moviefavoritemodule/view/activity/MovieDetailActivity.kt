package com.example.moviefavoritemodule.view.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.moviefavoritemodule.R
import com.example.moviefavoritemodule.data.model.Movie
import com.example.moviefavoritemodule.data.model.TvShow
import com.example.moviefavoritemodule.util.Const
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private var movie: Movie? = null
    private var tv: TvShow? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movie = intent.getParcelableExtra(Const.PARCEL_KEY_MOVIE)
        tv = intent.getParcelableExtra(Const.PARCEL_KEY_TV)
        bitmap = intent.getParcelableExtra(Const.PARCEL_KEY_BITMAP)

        setToView()

        val actionBar = supportActionBar
        var title: String?

        if (movie != null) {
            title = movie?.title
        } else {
            title = tv?.name
        }

        actionBar?.title = title
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setToView() {
        pbDetail.visibility = View.VISIBLE
        if (movie != null) {
            tvDetailYear.text = movie?.releaseDate
            tvDetailLanguage.text = movie?.language
            tvDetailRate.text = movie?.rating.toString()

            if (movie?.overview.isNullOrBlank()) {
                tvDetailOverview.text = resources.getString(R.string.msg_no_overview)
            } else {
                tvDetailOverview.text = movie?.overview
            }
        } else {
            tvDetailYear.text = tv?.firstAirDate
            tvDetailLanguage.text = tv?.language
            tvDetailRate.text = tv?.rating.toString()

            if (tv?.overview.isNullOrBlank()) {
                tvDetailOverview.text = resources.getString(R.string.msg_no_overview)
            } else {
                tvDetailOverview.text = tv?.overview
            }
        }

        ivDetailIcon.setImageBitmap(bitmap)
        pbDetail.visibility = View.GONE
    }
}
