package com.example.android.worldheadlines.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.worldheadlines.MainActivity;
import com.example.android.worldheadlines.R;
import com.squareup.picasso.Picasso;

public class HeadlinesWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context,
                                       AppWidgetManager appWidgetManager,
                                       String title,
                                       String image,
                                       int appWidgetId,
                                       int[] appWidgetIds){

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_app);

        if(image.equals("null") || image.equals("")) {
            int b = R.drawable.wh_icon;
            views.setImageViewResource(R.id.image_article_widget, b);
        }else{
            Picasso.with(context)
                    .load(image)
                    .resize(100, 100)
                    .centerCrop()
                    .into(views, R.id.image_article_widget, appWidgetIds);
        }

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setTextViewText(R.id.appwidget_text, title);
        views.setOnClickPendingIntent(R.id.appwidget_text, mainPendingIntent);

        Intent nextArticleIntent = new Intent(context, HeadlinesIntentService.class);
        nextArticleIntent.setAction(HeadlinesIntentService.NEXT_VIEW_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, nextArticleIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.next_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        HeadlinesIntentService.startActionWidget(context, appWidgetIds);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, String title, String image, int[] appWidgetsIds){
        for (int appWidgetId : appWidgetsIds) {
            updateAppWidget(context, appWidgetManager, title, image, appWidgetId, appWidgetsIds);
        }
    }
}

