package com.example.android.worldheadlines;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.worldheadlines.utilitaries.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SearchActivity extends AppCompatActivity {

    private EditText mKeyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_search);
    mToolbar.inflateMenu(R.menu.main_menu);
    setSupportActionBar(mToolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null)  ab.setDisplayHomeAsUpEnabled(true);
    mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.ice_white), PorterDuff.Mode.SRC_ATOP);

        FirebaseAnalytics mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);

    mKeyWord = (EditText) findViewById(R.id.search_view);
        TextView mSearchAction = (TextView) findViewById(R.id.search_button);

    mSearchAction.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View itemView){
            Context context = SearchActivity.this;
            Class<MainActivity> destination = MainActivity.class;
            String getWord = mKeyWord.getText().toString();
            Intent intent = new Intent(context, destination);

            if(!getWord.equals("")) {
                intent.putExtra(Constants.SEARCH_INTENT_KEY, getWord);
                intent.putExtra(Constants.SEARCH_INTENT_QUERY_KEY, true);
                View view = new View(getApplicationContext());
                startActivity((intent), ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, view, "transition").toBundle());
            }else {
                Toast.makeText(context, getResources().getText(R.string.invalid_word), Toast.LENGTH_LONG).show();
            }
        }
    });
    }
}
