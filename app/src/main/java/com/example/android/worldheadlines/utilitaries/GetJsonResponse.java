package com.example.android.worldheadlines.utilitaries;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.worldheadlines.database.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GetJsonResponse {

    public List<ArticleList> jsonReponse(Context context, String response, List<ArticleList> articleLists) throws JSONException {

        List<ArticleList> mArticleList = articleLists;
        Context mContext = context;

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("articles");

        for(int c = 0; c < jsonArray.length(); c++){

            JSONObject json = jsonArray.getJSONObject(c);

            JSONObject source = json.getJSONObject("source");

            ArticleList articles = new ArticleList(json.getString("title"),
                    json.getString("description"),
                    json.getString("url"),
                    json.getString("urlToImage"),
                    source.getString("name"),
                    json.getString("publishedAt"));
            mArticleList.add(articles);

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.HeadlinesEntry.COLUMN_TITLE, articles.getTitle());
            contentValues.put(Contract.HeadlinesEntry.COLUMN_DESCRIPTION, articles.getDescription());
            contentValues.put(Contract.HeadlinesEntry.COLUMN_URL, articles.getUrl());
            contentValues.put(Contract.HeadlinesEntry.COLUMN_IMAGE, articles.getImage());
            contentValues.put(Contract.HeadlinesEntry.COLUMN_SOURCE, articles.getSource());
            contentValues.put(Contract.HeadlinesEntry.COLUMN_DATE, articles.getDate());

            mContext.getContentResolver().insert(Contract.HeadlinesEntry.CONTENT_URI, contentValues);
        }
        return mArticleList;
    }
}
