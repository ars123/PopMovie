package com.example.user.app.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arun on 13-01-2017.
 */
public class Review implements Parcelable{


    /**
     * Reviews of the movie
     */
    private String mAuthor;
    private String mId;
    private String mContent;

    /**
     * Create an empty constructor so that an empty movie's object can be referenced
     * in the MainActivity for storing movie's info
     */
    public Review() {
    }

    public Review(JSONObject review) throws JSONException{

        mAuthor = review.getString("author");
        mContent = review.getString("content");
        mId = review.getString("url");

    }

    public String getId() { return mId; }

    public String getAuthor() { return mAuthor; }

    public String getContent() { return mContent; }


    protected Review(Parcel in) {

        mAuthor = in.readString();
        mContent = in.readString();
        mId = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mId);
    }
}
