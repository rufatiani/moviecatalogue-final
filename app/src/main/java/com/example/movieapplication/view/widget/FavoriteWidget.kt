package com.example.movieapplication.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import com.example.movieapplication.R

/**
 * Implementation of App Widget functionality.
 */
class FavoriteWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            if (intent.action.equals(TOAST_ACTION)) {
                val index = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view " + index, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        private val TOAST_ACTION = "com.example.movieapplication.TOAST_ACTION"
        val EXTRA_ITEM = "com.example.movieapplication.EXTRA_ITEM"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, FavoriteWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.favorite_widget)
            views.setRemoteAdapter(R.id.svWidget, intent)
            views.setEmptyView(R.id.svWidget, R.id.tvWidgetEmpty)

            val toastIntent = Intent(context, FavoriteWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val pendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.svWidget, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

