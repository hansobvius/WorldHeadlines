package com.example.android.worldheadlines;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.worldheadlines.adapter.MainAdapter;
import com.example.android.worldheadlines.database.Contract;
import com.example.android.worldheadlines.utilitaries.ArticleList;
import com.example.android.worldheadlines.utilitaries.Constants;
import com.example.android.worldheadlines.utilitaries.NetworkUtil;
import com.example.android.worldheadlines.utilitaries.UriBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Cursor mCursor;
    private Bundle mRecyclerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar_main);
        mToolBar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(mToolBar);

        Context context = MainActivity.this;
        List<ArticleList> mArticleList = new ArrayList<>();
        NetworkUtil networkUtil = new NetworkUtil();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        networkUtil.setProgressBar(mProgressBar);

        /**
         * Keyword Retrieve Data from SearchActivity
         */
        Intent wordIntent = getIntent();
        wordIntent.hasExtra(Constants.SEARCH_INTENT_KEY);
        String mGetWord = wordIntent.getStringExtra(Constants.SEARCH_INTENT_KEY);
        boolean mGetQuery = wordIntent.getBooleanExtra(Constants.SEARCH_INTENT_QUERY_KEY, false);
        if(mGetWord == null){
            mGetWord = "";
        }

        /**
         * Country selection Retrieve. If SharedPreference wasn´t used to store user´s preference,
         * a default value are invoked
         */
        Intent countryIntent = getIntent();
        countryIntent.hasExtra(Constants.DIALOG_INTENT_KEY);
        String mCountrySelected = countryIntent.getStringExtra(Constants.DIALOG_INTENT_KEY);
        String mCountryName = countryIntent.getStringExtra(Constants.DIALOG_NAME_KEY);
        if(mCountrySelected == null){
            mCountrySelected = Constants.COUNTRY_DEFAULT;
            mCountryName = this.getResources().getString(R.string.default_country_name);
        }else{
            setUserPreference(mCountrySelected, mCountryName);
        }
        if(getUserPreference() != null){
            Bundle bundle = getUserPreference();
            mCountrySelected = bundle.getString(Constants.GET_BUNDLE_CODE);
            mCountryName = bundle.getString(Constants.GET_BUNDLE_NAME);
        }

        /**
         * Insertion of the country name in the toolbar subtitle. If no country was selected, a default value are invoked
         * If data was provided by the search query activity, the keyword are displayed
         */
        if(!mGetQuery) {
            mToolBar.setSubtitle(this.getResources().getString(R.string.you_are_on) + " " + mCountryName);
        }else{
            mToolBar.setSubtitle(this.getResources().getString(R.string.query) + " " + mGetWord);
        }

        /**
         * Set invalid keyword text on the adapter. If the user insert a
         * invalid keyword, that will return a text adviser
         */
        TextView mInvalidKeyword = (TextView) findViewById(R.id.no_keyword_match);
        mInvalidKeyword.setVisibility(View.INVISIBLE);
        networkUtil.setInvalidText(mInvalidKeyword);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        String mUrl;
        if(mGetWord.equals("")){
            mUrl = getDefaultResult(mCountrySelected);
        }else{
            mUrl = getKeywordResult(mGetWord);
        }

        /**
         * If the device has a internet connection, the actual database are deleted and the daa are requery from http api.
         * if doesn´t the old data are preserved and used.
         */
        boolean b = internetConnection();
        if(b){
            cleanDataBase();
            networkUtil.getHttpConnection(context, mRecyclerView, mArticleList, mUrl);
        }else{
            getSupportLoaderManager().initLoader(Constants.LOADER_TASK_KEY, null, this);
            Toast.makeText(context, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mRecyclerBundle = new Bundle();
        Parcelable parcelable = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mRecyclerBundle.putParcelable(Constants.STATE_POSITION_KEY, parcelable);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mRecyclerBundle != null){
            Parcelable positionState = mRecyclerBundle.getParcelable(Constants.STATE_POSITION_KEY);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(positionState);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mCursor != null) {
            mCursor.close();
        }
    }

    /**
     * Loader class used to load Cursor data when the app are without internet connection
     */
    @NonNull
    @SuppressLint("StaticFieldLeak")
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs){
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor cursor = null;

            @Override
            protected void onStartLoading(){
                if(cursor != null){
                    deliverResult(mCursor);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                Context context = MainActivity.this;
                cursor = getDataCursor(context);
                return cursor;
            }

            @Override
            public void deliverResult(Cursor data){
                Cursor loadCursor = data;
                super.deliverResult(loadCursor);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor){
        mCursor = cursor;
        mProgressBar.setVisibility(View.INVISIBLE);
        Context context = MainActivity.this;
        MainAdapter mMainAdapter = new MainAdapter(context, mCursor);
        mRecyclerView.setAdapter(mMainAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {}

    /**
     * Check the internet connectivity
     * @return true if the app has internet connection, false if not
     */
    private boolean internetConnection(){
        Context context = MainActivity.this;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm != null ? cm.getActiveNetworkInfo() : null;
        boolean isConnected = ni != null && ni.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * If the app haven´t a intenet connection, the previous data saved are maintained and show to the user
     */
    private Cursor getDataCursor(Context context){
        Cursor cursor = context.getContentResolver().query(Contract.HeadlinesEntry.CONTENT_URI, Contract.HeadlinesEntry.PROJECTION,
                null, null, null);
        return cursor;
    }

    /**
     * Clean the data base for re-query data from internet. If the user haven´t internet connection,
     * this method aren´t used
     */
    private void cleanDataBase(){
        Uri uri = Contract.HeadlinesEntry.CONTENT_URI;
        getContentResolver().delete(uri, null, null);
    }

    /**
     * If is the first usage  by  user, the default lenguage are used
     */
    private String getDefaultResult(String s){
        String result = null;
        UriBuilder uriBuilder = new UriBuilder();
        result = uriBuilder.buildCountryPreferenceUri(s);
        return result;
    }

    /**
     * Get the keyword inserted by the user on SearchAcivity.class
     */
    private String getKeywordResult(String s) {
        String result = null;
        UriBuilder uriBuilder = new UriBuilder();
        result = uriBuilder.buildSearchUri(s);
        return result;
    }

    /**
     * Set country source by user preferences
     */
    private void setUserPreference(String s, String n){
        Context context = this;
        String keyPreferences = Constants.SHARED_PREFERENCES_KEY;
        String keyCodeName = Constants.SHARED_PREFERENCES_KEY;
        String keyCountryName = Constants.SHARED_PREFERENCES_NAME_KEY;
        SharedPreferences sharedPreferences = context.getSharedPreferences(keyPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editorCode = sharedPreferences.edit();
        SharedPreferences.Editor editorName = sharedPreferences.edit();
        editorCode.putString(keyCodeName, s);
        editorName.putString(keyCountryName, n);
        editorCode.apply();
        editorName.apply();
    }

    /**
     * Get country source by Preferences
     */
    private Bundle getUserPreference(){
        String keyPreferences = Constants.SHARED_PREFERENCES_KEY;
        String keyCodeName = Constants.SHARED_PREFERENCES_KEY;
        String keyCountryName = Constants.SHARED_PREFERENCES_NAME_KEY;
        String defaultCodeValue = Constants.COUNTRY_DEFAULT;
        String defaulNameValue = this.getResources().getString(R.string.default_country_name);
        SharedPreferences getSharedPreferences = this.getSharedPreferences(keyPreferences, MODE_PRIVATE);
        String getCode = getSharedPreferences.getString(keyCodeName, defaultCodeValue);
        String getName = getSharedPreferences.getString(keyCountryName, defaulNameValue);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.GET_BUNDLE_CODE, getCode);
        bundle.putString(Constants.GET_BUNDLE_NAME, getName);
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.search_option){
            View view = new View(this);
            startActivity(new Intent(this, SearchActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "transition").toBundle());
            return true;
        }
        if(id == R.id.country_preference){
            FragmentManager fm  = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LenguageFragment lenguageFragment = new LenguageFragment();
            lenguageFragment.show(fm, Constants.DIALOG_FRAGMENT_KEY);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
