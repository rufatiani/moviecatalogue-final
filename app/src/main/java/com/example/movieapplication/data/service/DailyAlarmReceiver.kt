package com.example.movieapplication.data.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.widget.Toast
import com.example.movieapplication.R
import com.example.movieapplication.utils.Const
import com.example.movieapplication.view.activity.HomeActivity
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DailyAlarmReceiver : BroadcastReceiver() {

    private var builder : NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra(Const.PARCEL_KEY_TITLE)
        val message = intent?.getStringExtra(Const.PARCEL_KEY_MESSAGE)

        showNofitication(context, title, message)
    }

    private fun showNofitication(context: Context?, title : String?, message : String?) {
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        builder = context?.let {
            NotificationCompat.Builder(it, Const.CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications_red_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_notifications_red_24dp))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(Const.CHANNEL_ID, Const.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            builder?.setChannelId(Const.CHANNEL_ID)
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = builder?.build()
        notificationManager?.notify(Const.NOTIFICATION_ID, notification)
    }

    fun setDailyReminder(context: Context){
        val alarmManager : AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        intent.putExtra(Const.PARCEL_KEY_TITLE, context.resources.getString(R.string.msg_daily_reminder))
        intent.putExtra(Const.PARCEL_KEY_MESSAGE, context.resources.getString(R.string.msg_daily_reminder_content))

        val calendar : Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, Const.ID_DAILY_REMINDER, intent, 0)
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    fun setReleasedReminder(context: Context){
        val alarmManager : AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        intent.putExtra(Const.PARCEL_KEY_TITLE, context.resources.getString(R.string.msg_released_reminder))
        intent.putExtra(Const.PARCEL_KEY_MESSAGE, context.resources.getString(R.string.msg_released_reminder_content))

        val calendar : Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, Const.ID_RELEASED_REMINDEER, intent, 0)
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    fun cancelAlarm(context: Context, code : Int){
        val alarmManager : AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, code, intent, 0)
        pendingIntent.cancel()

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent)
        }
    }

}