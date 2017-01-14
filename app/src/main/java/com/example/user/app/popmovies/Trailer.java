package com.example.user.app.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 13-01-2017.
 */
public class Trailer implements Parcelable {

    String mId;
    String mKey;
    String mName;
    String mSite;
    String mType;


    /**
     * Create an empty constructor so that an empty movie's object can be referenced
     * in the MainActivity for storing movie's info
     */
    public Trailer(){

    }

    public Trailer(JSONObject trailer) throws JSONException{
        mId = trailer.getString("id");
        mKey = trailer.getString("key");
        mName = trailer.getString("name");
        mSite = trailer.getString("site");
        mType = trailer.getString("type");
    }


    /*getter method*/
    public String getId() {
        return mId;
    }

    public String getKey() { return mKey; }

    public String getName() { return mName; }

    public String getSite() { return mSite; }

    public String getType() { return mType; }




    protected Trailer(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mType = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mId);
        parcel.writeString(mKey);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeString(mType);
    }
}
