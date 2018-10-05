package com.example.android.worldheadlines.utilitaries;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleList implements Parcelable {

    private String mTitle;
    private String mDesrcription;
    private String mUrl;
    private String mImage;
    private String mSource;
    private String mDate;

    public ArticleList(String title, String description, String url, String image, String source, String date){
        this.mTitle = title;
        this.mDesrcription = description;
        this.mUrl = url;
        this.mImage = image;
        this.mSource = source;
        this.mDate = date;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public String getTitle(){
        return this.mTitle;
    }

    public void setDescription(String description){
        this.mDesrcription = description;
    }

    public String getDescription(){
        return this.mDesrcription;
    }

    public void setUrl(String url){
        this.mUrl = url;
    }

    public String getUrl(){
        return this.mUrl;
    }

    public void setImage(String image){
        this.mImage = image;
    }

    public String getImage(){
        return this.mImage;
    }

    public void setSource(String source){
        this.mSource = source;
    }

    public String getSource(){
        return this.mSource;
    }

    public void setDate(String date){
        this.mDate = date;
    }

    public String getDate(){
        return this.mDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDesrcription);
        dest.writeString(mUrl);
        dest.writeString(mImage);
        dest.writeString(mSource);
        dest.writeString(mDate);
    }

    private ArticleList(Parcel in) {
        mTitle = in.readString();
        mDesrcription = in.readString();
        mUrl = in.readString();
        mImage = in.readString();
        mSource = in.readString();
        mDate = in.readString();
    }

    public static final Creator<ArticleList> CREATOR = new Creator<ArticleList>() {
        @Override
        public ArticleList createFromParcel(Parcel in) {
            return new ArticleList(in);
        }

        @Override
        public ArticleList[] newArray(int size) {
            return new ArticleList[size];
        }
    };
}
