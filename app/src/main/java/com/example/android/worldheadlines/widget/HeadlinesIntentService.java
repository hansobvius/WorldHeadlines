package com.example.android.worldheadlines.widget;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.worldheadlines.database.Contract;
import com.example.android.worldheadlines.utilitaries.StringManipulation;

public class HeadlinesIntentService extends IntentService {

    private static int mCount;
    private static int[] mAppWidgetIds;
    public static final String NEXT_VIEW_WIDGET = "com.example.android.worldheadlines.widget.action.article_list";

    private String s = "";
    private String i = "";

    public HeadlinesIntentService() {
        super("HeadlinesIntentService");
    }

    public static void startActionWidget(Context context, int[] appWidgetIds){
        mAppWidgetIds = appWidgetIds;
        Intent intent = new Intent(context, HeadlinesIntentService.class);
        intent.setAction(NEXT_VIEW_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       if(intent != null){
           final String action = intent.getAction();
           if(NEXT_VIEW_WIDGET.equals(action)){
               handleActionWidget(getApplicationContext());
           }
       }
    }

    private void handleActionWidget(final Context context){
        new AsyncCursor().execute(context);
    }

    private static Cursor getCursorResponse(Context context){
        Cursor cursor = context.getContentResolver().query(Contract.HeadlinesEntry.CONTENT_URI,
                Contract.HeadlinesEntry.PROJECTION, null, null, null);
        return cursor;
    }

    private String getDataFromCursor(Cursor cursor, int position){
        cursor.moveToPosition(position);
        int columnTitle = cursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_TITLE);
        String title = cursor.getString(columnTitle);
        int columnSource = cursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_SOURCE);
        String source = cursor.getString(columnSource);
        int columnDate = cursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_DATE);
        String date = cursor.getString(columnDate);
        StringManipulation stringManipulation = new StringManipulation();
        String dateManipulated = stringManipulation.getFormatedString(date);
        String formatedString = title + "\n\n" + source + "\n" + dateManipulated;
        return formatedString;
    }

    private String getImageFromCursor(Cursor cursor, int position){
        cursor.moveToPosition(position);
        int columnImage = cursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_IMAGE);
        String image = cursor.getString(columnImage);
        return image;
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCursor extends AsyncTask<Context, Void, Cursor> {

        Context mContext;

        @Override
        public Cursor doInBackground(Context... contexts){
            Context context = contexts[0];
            mContext = context;
            Cursor cursorResponse = getCursorResponse(context);
            return cursorResponse;
        }

        @Override
        public void onPostExecute(Cursor cursor){

            ComponentName componentName = new ComponentName(mContext, HeadlinesWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            int cursorSize = cursor.getCount();
            if(mCount > cursorSize - 1){
                mCount = 0;
            }

            if(cursor.getCount() > 0) {
                s = getDataFromCursor(cursor, mCount);
                i = getImageFromCursor(cursor, mCount);
                mCount++;
            }

            HeadlinesWidgetProvider.updateWidget(mContext, appWidgetManager, s, i, mAppWidgetIds != null ? mAppWidgetIds : appWidgetIds);
        }
    }
}
