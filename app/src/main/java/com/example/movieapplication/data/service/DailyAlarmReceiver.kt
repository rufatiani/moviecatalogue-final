package com.example.movieapplication.data.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.support.annotation.CallSuper
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.widget.Toast
import com.example.movieapplication.R
import com.example.movieapplication.data.model.Movie
import com.example.movieapplication.data.model.PageMovie
import com.example.movieapplication.data.repository.MovieRepository
import com.example.movieapplication.data.service.task.DownloadImageTask
import com.example.movieapplication.utils.Const
import com.example.movieapplication.view.activity.HomeActivity
import com.example.movieapplication.view.activity.MovieDetailActivity
import dagger.android.AndroidInjection
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DailyAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var movieRepository: MovieRepository

    private var builder : NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private var type : String? = Const.DAILY_REMINDER_KEY
    private var list : List<Movie> = ArrayList()

    override fun onReceive(context: Context, intent: Intent?) {
        AndroidInjection.inject(this, context)

        val title = intent?.getStringExtra(Const.PARCEL_KEY_TITLE)
        val message = intent?.getStringExtra(Const.PARCEL_KEY_MESSAGE)
        type = intent?.getStringExtra(Const.PARCEL_KEY_TYPE)

        if (type.equals(Const.DAILY_REMINDER_KEY)){
            showNofitication(context, title, message, Const.NOTIFICATION_ID)
        }else{
            val today = SimpleDateFormat(Const.FORMAT_DATE).format(Date())
            list = ReleasedMovieTask(movieRepository).execute(today).get().results
            for (i in 1..list.size){
                showNofitication(context, title, list[i-1].title + " " + message, i)
            }
        }
    }

    private fun showNofitication(context: Context?, title : String?, message : String?, notifId : Int) {
        var intent = Intent(context, HomeActivity::class.java)
        if(type.equals(Const.RELEASED_REMINDER_KEY)){
            val i = notifId-1

            intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(Const.PARCEL_KEY_FAVORITE, false)
            intent.putExtra(Const.PARCEL_KEY_MOVIE, list[i])

            val bitmap : Bitmap? = DownloadImageTask().execute(Const.URL_IMAGE + Const.URL_IMAGE_SIZE + list[i].image).get()
            intent.putExtra(Const.PARCEL_KEY_BITMAP, bitmap)
        }

        val pendingIntent = PendingIntent.getActivity(context, notifId, intent, 0)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(Const.CHANNEL_ID, Const.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            builder?.setChannelId(Const.CHANNEL_ID)
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = builder?.build()
        notificationManager?.notify(notifId, notification)
    }

    fun setDailyReminder(context: Context){
        val alarmManager : AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        intent.putExtra(Const.PARCEL_KEY_TITLE, context.resources.getString(R.string.msg_daily_reminder))
        intent.putExtra(Const.PARCEL_KEY_MESSAGE, context.resources.getString(R.string.msg_daily_reminder_content))
        intent.putExtra(Const.PARCEL_KEY_TYPE, Const.DAILY_REMINDER_KEY)

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
        intent.putExtra(Const.PARCEL_KEY_TYPE, Const.RELEASED_REMINDER_KEY)

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

    class ReleasedMovieTask(val movieRepository: MovieRepository) : AsyncTask<String, Void, PageMovie>() {
        override fun doInBackground(vararg params: String?): PageMovie? {
            var pageMovie : PageMovie? = null
            params[0]?.let { it ->
                movieRepository.movieRelease(it).subscribe({
                pageMovie = it
            },{
                    Log.e("ERROR", it.message)
            }) }

            return pageMovie
        }
    }
}