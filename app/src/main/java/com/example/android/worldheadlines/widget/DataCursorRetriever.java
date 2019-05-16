package com.example.android.worldheadlines.widget;

import android.content.Context;
import android.database.Cursor;

import com.example.android.worldheadlines.database.Contract;
import com.example.android.worldheadlines.utilitaries.StringManipulation;

public class DataCursorRetriever {

    public String getDataFromCursor(Cursor cursor, int position){
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

    public String getImageFromCursor(Cursor cursor, int position){
        cursor.moveToPosition(position);
        int columnImage = cursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_IMAGE);
        String image = cursor.getString(columnImage);
        return image;
    }

    public static Cursor getCursorResponse(Context context){
        Cursor cursor = context.getContentResolver().query(Contract.HeadlinesEntry.CONTENT_URI,
                Contract.HeadlinesEntry.PROJECTION, null, null, null);
        return cursor;
    }

}
