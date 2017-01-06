package com.example.user.app.popmovies;

/**
 * Created by User on 05-01-2017.
 */
public class Utility {

    public static String buildImageUrl(int width, String fileName) {
        return "http://image.tmdb.org/t/p/w" + Integer.toString(width) + fileName;
    }
}
