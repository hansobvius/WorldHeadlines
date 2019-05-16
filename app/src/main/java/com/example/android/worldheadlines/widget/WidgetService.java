package com.example.android.worldheadlines.widget;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.camera2.CaptureRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.android.worldheadlines.R;

public class WidgetService extends Service {

    public static final String NEXT_VIEW_WIDGET = "com.example.android.worldheadlines.widget.action.article_list";

    //private static final String CHANNEL_ID = "channel_id";

    private static int mCount;
    private Cursor mCursor;

    private DataCursorRetriever mDataCursorRetriever;
    //private NotificationBuilder mNotificationBuilder;

    private String s = "";
    private String i = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mDataCursorRetriever = new DataCursorRetriever();
        //mNotificationBuilder = new NotificationBuilder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mNotificationBuilder.setContext(getApplicationContext());
            startForeground(flags, mNotificationBuilder);
        }
        */
        if(intent != null){
            final String action = intent.getAction();
            if(NEXT_VIEW_WIDGET.equals(action)){
                new AsyncCursor().execute(getApplicationContext());
            }
        }
        return Service.START_STICKY;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCursor != null) mCursor.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCursor extends AsyncTask<Context, Void, Cursor> {

        Context mContext;

        @Override
        public Cursor doInBackground(Context... contexts){
            Context context = contexts[0];
            mContext = context;
            Cursor cursorResponse = mDataCursorRetriever.getCursorResponse(context);
            return cursorResponse;
        }

        @Override
        public void onPostExecute(Cursor cursor){

            mCursor = cursor;

            ComponentName componentName = new ComponentName(mContext, HeadlinesWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            int cursorSize = mCursor.getCount();
            if(mCount > cursorSize - 1) mCount = 0;

            if(mCursor.getCount() > 0) {
                s = mDataCursorRetriever.getDataFromCursor(mCursor, mCount);
                i = mDataCursorRetriever.getImageFromCursor(mCursor, mCount);
                mCount++;
            }

            HeadlinesWidgetProvider.updateWidget(mContext, appWidgetManager, s, i, appWidgetIds);
        }
    }
}
