package com.example.android.worldheadlines.utilitaries;

import android.net.Uri;

import static com.example.android.worldheadlines.utilitaries.Constants.*;

public class UriBuilder {

    public String buildCountryPreferenceUri(String query){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(COUNTRY, query)
                .appendQueryParameter(KEY_QUERY, KEY)
                .build();
        String uriString = uri.toString();

        return uriString;
    }

    public String buildSearchUri(String query){
        Uri uri = Uri.parse(SEARCH_BASE_QUERY).buildUpon()
                .appendQueryParameter(PARAM_QUERY, query)
                .appendQueryParameter(KEY_QUERY, KEY)
                .build();
        String uriString = uri.toString();

        return uriString;
    }
}
