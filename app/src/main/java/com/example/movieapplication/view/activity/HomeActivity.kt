package com.example.movieapplication.view.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.NotificationCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.movieapplication.R
import com.example.movieapplication.data.adapter.PagerBottomNavigationAdapter
import com.example.movieapplication.data.service.DailyAlarmReceiver
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.Preferences
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var prevMenuItem: MenuItem? = null
    private lateinit var dailyAlarmReceiver : DailyAlarmReceiver
    private lateinit var builder : NotificationCompat.Builder
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        prepareBroadcast()

        val fragmentAdapter = supportFragmentManager?.let { PagerBottomNavigationAdapter(baseContext, it) }
        vpHome.adapter = fragmentAdapter
        vpHome.offscreenPageLimit = 3
        vpHome.currentItem = 0

        vpHome.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem?.isChecked = false
                } else {
                    nav_view.menu.getItem(0).isChecked = false
                }

                nav_view.menu.getItem(p0).isChecked = true
                prevMenuItem = nav_view.menu.getItem(p0)
            }

        })
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_movie -> {
                vpHome.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tvshow -> {
                vpHome.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                vpHome.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { Preferences.setLocale(it) })
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
        } else if (item?.itemId == R.id.setting){
            val intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItemFavorite: MenuItem? = menu?.findItem(R.id.favorite)
        if (menuItemFavorite != null) {
            menuItemFavorite.isVisible = false
        }

        val menuItemSearch : MenuItem? = menu?.findItem(R.id.search)
        if (menuItemSearch != null){
            menuItemSearch.isVisible = true
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

    private fun prepareBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dailyAlarmReceiver = DailyAlarmReceiver()

            if (Preferences.getDailyPref(this)) {
                dailyAlarmReceiver.setDailyReminder(this)
            }

            if (Preferences.getReleasedPref(this)) {
                dailyAlarmReceiver.setReleasedReminder(this)
            }
        }
    }
}
