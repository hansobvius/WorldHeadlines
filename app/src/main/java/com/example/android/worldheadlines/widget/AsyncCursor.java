package com.example.android.worldheadlines.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

class AsyncCursor extends AsyncTask<Context, Void, Cursor> {

    private static int mCount;
    private Cursor mCursor;
    Context mContext;
    private DataCursorRetriever mDataCursorRetriever = new DataCursorRetriever();
    private String s = "";
    private String i = "";

    /*
    public AsyncCursor(Context context){
        this.mContext = context;
    }
    */

    public Cursor getAsyncCursor(){
        return mCursor;
    }

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