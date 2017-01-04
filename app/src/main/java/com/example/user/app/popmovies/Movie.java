package com.example.user.app.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 04-01-2017.
 */
public class Movie implements Parcelable {



    /**
     * Create an empty constructor so that an empty movie's object can be referenced
     * in the MainActivity for storing movie's info
     */
    public Movie() {
    }
    /**
     * Title of the movie
     */
    private String mMovieTitle;
    /*Variables needed when handling recycler view is clicked*/
    /**
     * posterPath of the movie
     */
    private String mMoviePosterPath;
    /**
     * id of the movie
     */
    private int mMovieId;
    /**
     * BackdropPosterPath of the movie
     */
    private String mMovieBackdropPath;
    private double mMovieVoteCount;
    private String mMovieOverview;
    private double mMovieVoteAverage;
    private String mMovieOriginalTitle;
    private double mMoviePopularity;
    private String mMovieReleaseDate;
    private int mMovieRunTimeDuration;

    /**
     * Constructs a new {@link Movie}.
     *
     * @param movieTitle       is the Title of the movie
     * @param posterPath       is the Poster Path of the movie
     * @param id               is the id of the movie
     * @param overview         is the Overview of the movie
     * @param voteCount        is the Vote Count of the movie
     * @param originalTitle    is the Original Title of the movie
     * @param voteAverage      is the Vote Average of the movie
     * @param popularity       is the Popularity of the movie
     * @param backdropPath     is the Backdrop Path of the movie
     * @param movieReleaseDate is the movie Release Date of the movie
     */
    public Movie(String movieTitle, int id, String posterPath, String overview, double voteCount, String originalTitle,
                 double voteAverage, double popularity, String backdropPath, String movieReleaseDate) {
        mMovieTitle = movieTitle;
        mMoviePosterPath = posterPath;
        mMovieId = id;
        mMovieOverview = overview;
        mMovieVoteCount = voteCount;
        mMovieOriginalTitle = originalTitle;
        mMovieVoteAverage = voteAverage;
        mMoviePopularity = popularity;
        mMovieBackdropPath = backdropPath;
        mMovieReleaseDate = movieReleaseDate;

    }

    public Movie(int runTimeDuration) {
        mMovieRunTimeDuration = runTimeDuration;
    }

    private Movie(Parcel in) {
        mMovieTitle = in.readString();
        mMovieId = in.readInt();
        mMoviePosterPath = in.readString();
        mMovieOverview = in.readString();
        mMovieVoteCount = in.readDouble();
        mMovieOriginalTitle = in.readString();
        mMovieVoteAverage = in.readDouble();
        mMoviePopularity = in.readDouble();
        mMovieBackdropPath = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieRunTimeDuration = in.readInt();
    }




    public void setMovieRunTimeDuration(int movieDuration) {
        mMovieRunTimeDuration = movieDuration;
    }

    /**
     * The Getters Methods
     */
    public String getMovieTitle() {
        return mMovieTitle;
    }

    /**
     * The Setters Methods
     */
    public void setMovieTitle(String title) {
        mMovieTitle = title;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int id) {
        mMovieId = id;
    }

    public String getMoviePosterPath() {
        return mMoviePosterPath;
    }

    public void setMoviePosterPath(String path) {
        mMoviePosterPath = path;
    }

    public String getMovieBackdropPath() {
        return mMovieBackdropPath;
    }

    public String getMovieOverview() {
        return mMovieOverview;
    }

    public double getMovieVoteCount() {
        return mMovieVoteCount;
    }

    public String getMovieOriginalTitle() {
        return mMovieOriginalTitle;
    }

    public double getMovieVoteAverage() {
        return mMovieVoteAverage;
    }

    public double getMoviePopularity() {
        return mMoviePopularity;
    }

    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public int getMovieRuntimeDuration() {
        return mMovieRunTimeDuration;
    }



    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(mMovieTitle);
        out.writeInt(mMovieId);
        out.writeString(mMoviePosterPath);
        out.writeString(mMovieOverview);
        out.writeDouble(mMovieVoteCount);
        out.writeString(mMovieOriginalTitle);
        out.writeDouble(mMovieVoteAverage);
        out.writeDouble(mMoviePopularity);
        out.writeString(mMovieBackdropPath);
        out.writeString(mMovieReleaseDate);
        out.writeInt(mMovieRunTimeDuration);

    }
}
