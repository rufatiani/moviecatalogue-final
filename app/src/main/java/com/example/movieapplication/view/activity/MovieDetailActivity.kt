package com.example.movieapplication.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.movieapplication.R
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.model.TvShow
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.Preferences
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModel
import com.example.movieapplication.viewmodel.movie.MovieFavoriteViewModelFactory
import com.example.movieapplication.viewmodel.tvshow.TvShowFavoriteViewModel
import com.example.movieapplication.viewmodel.tvshow.TvShowFavoriteViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_movie_detail.*
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity() {

    private var movie: Movie? = null
    private var tv: TvShow? = null
    private var bitmap: Bitmap? = null
    private var isFavorite: Boolean = false

    @Inject
    lateinit var movieFavoriteViewModel: MovieFavoriteViewModel

    @Inject
    lateinit var movieFavoriteViewModelFactory: MovieFavoriteViewModelFactory

    @Inject
    lateinit var tvShowFavoriteViewModel: TvShowFavoriteViewModel

    @Inject
    lateinit var tvShowFavoriteViewModelFactory: TvShowFavoriteViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        AndroidInjection.inject(this)

        isFavorite = intent.getBooleanExtra(Const.PARCEL_KEY_FAVORITE, false)
        movie = intent.getParcelableExtra(Const.PARCEL_KEY_MOVIE)
        tv = intent.getParcelableExtra(Const.PARCEL_KEY_TV)
        bitmap = intent.getParcelableExtra(Const.PARCEL_KEY_BITMAP)

        setToView()

        val actionBar = supportActionBar
        var title: String?

        if (movie != null) {
            title = movie?.title
            movieFavoriteViewModel =
                ViewModelProviders.of(this, movieFavoriteViewModelFactory).get(MovieFavoriteViewModel::class.java)
        } else {
            title = tv?.name
            tvShowFavoriteViewModel =
                ViewModelProviders.of(this, tvShowFavoriteViewModelFactory).get(TvShowFavoriteViewModel::class.java)
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
            tvDetailLanguage.text = movie?.language?.let { Preferences.getLanguage(applicationContext, it) }
            tvDetailRate.text = movie?.rating.toString()

            if (movie?.overview.isNullOrBlank()) {
                tvDetailOverview.text = resources.getString(R.string.msg_no_overview)
            } else {
                tvDetailOverview.text = movie?.overview
            }
        } else {
            tvDetailYear.text = tv?.firstAirDate
            tvDetailLanguage.text = tv?.language?.let { Preferences.getLanguage(applicationContext, it) }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.set_lang_english) {
            settingLanguage(Const.LANGUAGE_KEY_EN)
        } else if (item?.itemId == R.id.set_lang_bahasa) {
            settingLanguage(Const.LANGUAGE_KEY_ID)
        } else if (item?.itemId == R.id.favorite) {
            if (movie != null) {
                movie?.let { it -> movieFavoriteViewModel.saveMovie(it) }

                if (!movieFavoriteViewModel.isError()) {
                    Toast.makeText(baseContext, resources.getString(R.string.msg_favorite), Toast.LENGTH_SHORT).show()
                }
            } else if (tv != null) {
                tv?.let { it -> tvShowFavoriteViewModel.savetvShow(it) }

                if (!movieFavoriteViewModel.isError()) {
                    Toast.makeText(baseContext, resources.getString(R.string.msg_favorite), Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItemFavorite: MenuItem? = menu?.findItem(R.id.favorite)
        if (menuItemFavorite != null) {
            menuItemFavorite.isVisible = !isFavorite
        }

        val menuItemSearch : MenuItem? = menu?.findItem(R.id.search)
        if (menuItemSearch != null){
            menuItemSearch.isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun settingLanguage(language: String) {
        Preferences.setLanguagePref(this, language)
        Preferences.setLocale(this)
        intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
