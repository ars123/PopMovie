package com.example.user.app.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 04-01-2017.
 */
public class Movie implements Parcelable {



    private int id;
    private String title; // original_title
    private String posterImage; // poster_path
    private String backDropImage; // backdrop_path
    private String overview;
    private int rating; // vote_average
    private String date; // release_date

    public Movie() {

    }

    public Movie(JSONObject movie) throws JSONException {
        this.id = movie.getInt("id");
        this.title = movie.getString("original_title");
        this.posterImage = movie.getString("poster_path");
        this.backDropImage = movie.getString("backdrop_path");
        this.overview = movie.getString("overview");
        this.rating = movie.getInt("vote_average");
        this.date = movie.getString("release_date");
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getBackDropImage() {
        return backDropImage;
    }

    public String getOverview() {
        return overview;
    }

    public int getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterImage);
        dest.writeString(backDropImage);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(date);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterImage = in.readString();
        backDropImage = in.readString();
        overview = in.readString();
        rating = in.readInt();
        date = in.readString();
    }
}
