package com.example.user.app.popmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 01-01-2017.
 */
public class MovieAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Movie> mMovieList = new ArrayList<Movie>();

    public MovieAdapter(LayoutInflater inf, ArrayList<Movie> imagelist){
        mMovieList=imagelist;
        inflater=inf;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMovieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        Movie currentMovie = mMovieList.get(i);
        if (view == null) {
            // if it's not recycled, create a new ImageView
            imageView = new ImageView(inflater.getContext());
        } else {
            imageView=(ImageView)view;
        }
        String url = "https://image.tmdb.org/t/p/w500/" + currentMovie.getMoviePosterPath().toString();
        Picasso.with(inflater.getContext()).load(url).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}
