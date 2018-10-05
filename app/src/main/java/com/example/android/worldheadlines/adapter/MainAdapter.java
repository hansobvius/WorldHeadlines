package com.example.android.worldheadlines.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.worldheadlines.R;
import com.example.android.worldheadlines.WebActivity;
import com.example.android.worldheadlines.database.Contract;
import com.example.android.worldheadlines.utilitaries.Constants;
import com.example.android.worldheadlines.utilitaries.StringManipulation;
import com.squareup.picasso.Picasso;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.NumberViewHolder> {

    private final Context mContext;
    private final Cursor mCursor;
    private ViewGroup.LayoutParams params = null;

    public MainAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    private final StringManipulation stringManipulation = new StringManipulation();

    @NonNull
    @Override
    public MainAdapter.NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false);
        return new NumberViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position){

        mCursor.moveToPosition(position);
        int cursorSize = mCursor.getCount();

        int title = mCursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_TITLE);
        int description = mCursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_DESCRIPTION);
        int url = mCursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_URL);
        int image = mCursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_IMAGE);
        int source = mCursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_SOURCE);
        int date = mCursor.getColumnIndex(Contract.HeadlinesEntry.COLUMN_DATE);

        String mTitle = mCursor.getString(title);
        String mDescription = mCursor.getString(description);
        String mUrl = mCursor.getString(url);
        String mImage = mCursor.getString(image);
        String mSource = mCursor.getString(source);
        String mDate = mCursor.getString(date);

        final int finalCursor = cursorSize - 2;
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) holder.mConstraintLayout.getLayoutParams();
        if(finalCursor >= position){
            marginLayoutParams.bottomMargin = 100;
        }else{
            marginLayoutParams.bottomMargin = 60;
        }

        Picasso.with(mContext).load(mImage).placeholder(R.drawable.wh_background_dark).into(holder.mImageView);

        holder.mTitleView.setText(mTitle);
        holder.setUrl(mUrl);
        holder.mSourceView.setText(mSource);
        holder.mDateView.setText(stringManipulation.getFormatedString(mDate));

        if(!mDescription.equals("null")){
            holder.mDescriptionView.setText(mDescription);
            holder.mDescriptionView.setTextAppearance(R.style.NewsDescription);
        }
        else{
            holder.mDescriptionView.setText(mContext.getResources().getString(R.string.no_description));
            holder.mDescriptionView.setTextAppearance(R.style.NewsNoDescription);
        }
    }

    @Override
    public int getItemCount(){
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder{

        final ConstraintLayout mConstraintLayout;
        final ImageView mImageView;
        final TextView mTitleView;
        final TextView mSourceView;
        final TextView mDateView;
        final TextView mDescriptionView;
        final Button mButtonWeb;
        final Button mButtonShare;
        String toUrl;

        private NumberViewHolder(final View view){
            super(view);

            mConstraintLayout = (ConstraintLayout) view.findViewById(R.id.article_box);
            mImageView = (ImageView) view.findViewById(R.id.image_article);
            mTitleView = (TextView) view.findViewById(R.id.title_view);
            mDateView = (TextView) view.findViewById(R.id.release_date);
            mSourceView = (TextView) view.findViewById(R.id.source_text);
            mDescriptionView = (TextView) view.findViewById(R.id.description_view);
            mButtonShare = (Button) view.findViewById(R.id.share_button);
            mButtonWeb = (Button) view.findViewById(R.id.website_button);

            mButtonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, toUrl);
                    mContext.startActivity(sendIntent);
                }
            });

            mButtonWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(Constants.URL_WEB_KEY, toUrl);
                    mContext.startActivity(intent);
                }
            });
        }

        private void setUrl(String s){
            toUrl = s;
        }
    }
}
