package com.example.movieapplication.view.activity

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.movieapplication.R
import com.example.movieapplication.data.service.DailyAlarmReceiver
import com.example.movieapplication.utils.Const
import com.example.movieapplication.utils.Preferences
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var dailyAlarmReceiver: DailyAlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reminder)

        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.var_setting)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        prepareSwitch()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dailyAlarmReceiver = DailyAlarmReceiver()

            swDailyReminder.setOnCheckedChangeListener { button, checked ->
                Preferences.setDailyPref(this, checked)

                if (checked) {
                    dailyAlarmReceiver.setDailyReminder(this)
                } else {
                    dailyAlarmReceiver.cancelAlarm(this, Const.ID_DAILY_REMINDER)
                }
            }

            swReleaseReminder.setOnCheckedChangeListener { button, checked ->
                Preferences.setReleasedPref(this, checked)
                if (checked) {
                    dailyAlarmReceiver.setReleasedReminder(this)
                } else {
                    dailyAlarmReceiver.cancelAlarm(this, Const.ID_RELEASED_REMINDEER)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        prepareSwitch()
    }

    private fun prepareSwitch() {
        swDailyReminder.isChecked = Preferences.getDailyPref(this)
        swReleaseReminder.isChecked = Preferences.getReleasedPref(this)
    }
}
