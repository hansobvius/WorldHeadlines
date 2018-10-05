package com.example.android.worldheadlines.utilitaries;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.worldheadlines.adapter.MainAdapter;
import com.example.android.worldheadlines.database.Contract;

import org.json.JSONException;

import java.util.List;

public class NetworkUtil {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<ArticleList> mArticleList;
    private Activity mActivity;
    private TextView mInvalidKeyword;
    private ProgressBar mProgressBar;

    private final GetJsonResponse getJsonResponse = new GetJsonResponse();

    public void setInvalidText(TextView t){
        this.mInvalidKeyword = t;
    }

    public void setProgressBar(ProgressBar progressBar){
        this.mProgressBar = progressBar;
    }

    public void getHttpConnection(Context context,
                                  RecyclerView recyclerView,
                                  final List<ArticleList> articleList,
                                  String url){

        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mArticleList = articleList;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    mArticleList = getJsonResponse.jsonReponse(mContext, response, mArticleList);
                    mProgressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Cursor cursor = mContext.getContentResolver().query(Contract.HeadlinesEntry.CONTENT_URI,
                        Contract.HeadlinesEntry.PROJECTION, null, null, null);

                httpResponse(mArticleList, cursor);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    /**
     * This method is used to check the validate of the keyword inserted by the user on search query activity.
     * If the keyword have no match with API response, a error message will be displayed on the screen; if else the
     * adapter will be activated and data populated
     */
    private void httpResponse(List<ArticleList> articleLists, Cursor cursor){
        boolean b = articleLists.isEmpty();
        if(b){
            mInvalidKeyword.setVisibility(View.VISIBLE);
        }else {
            MainAdapter mMainAdapter = new MainAdapter(mContext, cursor);
            mRecyclerView.setAdapter(mMainAdapter);
        }
    }
}
