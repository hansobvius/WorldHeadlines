package com.example.android.worldheadlines.utilitaries;

public class Constants {

    /**
     * Constants for Main and Search User Query
     */
    public static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_DEFAULT = "us";

    public static final String SEARCH_BASE_QUERY  = "https://newsapi.org/v2/everything";
    public static final String PARAM_QUERY = "q";

    public static final String KEY_QUERY =  "apiKey";
    public static final String KEY = "API USER KEY";

    /**
     * String keys for Dialog Fragement
     */
    public static final String DIALOG_FRAGMENT_KEY = "dialog_fragement_key";
    public static final String DIALOG_INTENT_KEY = "dialog_intent_key";
    public static final String DIALOG_NAME_KEY = "dialog_position_key";

    /**
     * String key for search intent request
     */
    public static final String SEARCH_INTENT_KEY = "search_intent_key";
    public static final String SEARCH_INTENT_QUERY_KEY = "search_query_key";

    /**
     * String key used to start WebView
     */
    public static final String URL_WEB_KEY = "web_url_key";

    /**
     * String keys for shared preferences/onSaveInstanceState code access
     */
    public static final String SHARED_PREFERENCES_KEY = "shared_preferences_key";
    public static final String SHARED_PREFERENCES_NAME_KEY = "shared_preferences_name_key";
    public static final String STATE_POSITION_KEY = "state_position_key";

    /**
     * String keys for Bundle data retrieve
     */
    public static final String GET_BUNDLE_CODE = "get_bundle_code";
    public static final String GET_BUNDLE_NAME = "get_bundle_name";

    /**
     * String int key to Main AsyncTaskLoader call
     */
    public static final int LOADER_TASK_KEY = 0;
}
