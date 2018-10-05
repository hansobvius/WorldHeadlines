package com.example.android.worldheadlines.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String AUTHORITY = "com.example.android.worldheadlines.database";

    private static final Uri URI_BASE = Uri.parse("content://" + AUTHORITY);

    public static final String PATH = "headlinesDB";

    public static final long NO_ARTICLE_ID = -1;

    public static final class HeadlinesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = URI_BASE.buildUpon().appendPath(PATH).build();

        public static final String TABLE_NAME = "headlinesDB";
        public static final String _ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_DATE = "date";

        public static final String[] PROJECTION = new String[]{
                HeadlinesEntry._ID,
                HeadlinesEntry.COLUMN_TITLE,
                HeadlinesEntry.COLUMN_DESCRIPTION,
                HeadlinesEntry.COLUMN_URL,
                HeadlinesEntry.COLUMN_IMAGE,
                HeadlinesEntry.COLUMN_SOURCE,
                HeadlinesEntry.COLUMN_DATE
        };
    }
}
