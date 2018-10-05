package com.example.android.worldheadlines.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.worldheadlines.database.Contract.HeadlinesEntry.TABLE_NAME;

public class HeadlinesProvider extends ContentProvider{

    private DataBase mDataBase;
    private static final int HEADLINE_TASK = 100;
    private static final int HEADLINE_TASK_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH, HEADLINE_TASK);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH + "/#", HEADLINE_TASK_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDataBase = new DataBase(context);
        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mDataBase.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case HEADLINE_TASK: {
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract.HeadlinesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow Uri ".concat(String.valueOf(uri)));
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mDataBase.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match){
            case HEADLINE_TASK:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case HEADLINE_TASK_ID:
                String id = uri.getPathSegments().get(1);
                projection = Contract.HeadlinesEntry.PROJECTION;

                /**
                 *  Esta atribuição para o parametro selection do Query nao funciona pq o _ID criado pela classe BaseColumn Incrementa
                 *  continuamente o valor detsa coluna, mesmo se os dados forem deletados. Como este Query nao é ultilizado em nenhum
                 *  outra instancia no app, o codigo foi mantido por questões didáticas
                 */
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDataBase.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        int headlineDelete;

        switch (match){
            case HEADLINE_TASK:
                headlineDelete = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case HEADLINE_TASK_ID:
                String id = uri.getPathSegments().get(1);
                headlineDelete = db.delete(TABLE_NAME, Contract.HeadlinesEntry._ID + " = ?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }
        return headlineDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
