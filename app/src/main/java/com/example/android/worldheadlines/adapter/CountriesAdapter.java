package com.example.android.worldheadlines.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.worldheadlines.MainActivity;
import com.example.android.worldheadlines.R;
import com.example.android.worldheadlines.utilitaries.Constants;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    private final String[] mCountryCode;
    private final String[] mCountryName;
    private final Context mContext;
    private Activity mActivity;

    public CountriesAdapter(String[] cc, String[] cn, Context context, Activity activity){
        this.mCountryCode = cc;
        this.mCountryName = cn;
        this.mContext = context;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.country_list, parent, false);
        return new CountriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, int position){
        String cn = mCountryName[position];
        holder.mCountry.setText(cn);

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) holder.mCountry.getLayoutParams();
        if(position > 0){
            marginLayoutParams.topMargin = 12;
        }
    }

    @Override
    public int getItemCount(){
        return mCountryName.length;
    }

    public class CountriesViewHolder extends RecyclerView.ViewHolder{

        final TextView mCountry;

        private CountriesViewHolder(View view){
            super(view);
            mCountry = (TextView) view.findViewById(R.id.cn_list);

            mCountry.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View item){
                    int position = getAdapterPosition();
                    String s = mCountryCode[position];
                    String n = mCountryName[position];
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(Constants.DIALOG_INTENT_KEY, s);
                    intent.putExtra(Constants.DIALOG_NAME_KEY, n);
                    mContext.startActivity((intent),
                            ActivityOptions.makeSceneTransitionAnimation(mActivity, item, "transition").toBundle());
                }
            });
        }
    }
}
