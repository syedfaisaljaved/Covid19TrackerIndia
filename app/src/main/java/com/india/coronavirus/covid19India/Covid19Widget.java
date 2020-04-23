package com.india.coronavirus.covid19India;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class Covid19Widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int nOfWidgets = appWidgetIds.length;

        for (int i = 0; i < nOfWidgets; i++) {

            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, Covid19BackgroundService.class);
            intent.putExtra("appWidgetId", appWidgetId);
            context.startService(intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

