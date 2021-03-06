package com.example.user.app.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 11-02-2017.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY="com.example.user.app.popmovies";
    public static final Uri BASE_CONTENT_URL = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String MOVIE_PATH="movie";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon().appendPath(MOVIE_PATH).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"+ CONTENT_AUTHORITY + "/"+ MOVIE_PATH;
        public static final String CONTENT_BASE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_IMAGE = "image";
        public static final String COLUMN_THUMB_IMAGE = "image2";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DATE = "date";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

}
