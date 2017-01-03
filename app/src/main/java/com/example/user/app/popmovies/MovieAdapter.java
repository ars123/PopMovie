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
    private List<?> mImageList = new ArrayList<>();
    private Context mContext;
    private int mLayout;
    private int mId;

    public MovieAdapter(LayoutInflater inf, List<?> imagelist){
        mImageList=imagelist;
        inflater=inf;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int i) {
        return mImageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = null;
        if (view == null) {
            // if it's not recycled, create a new ImageView
            imageView = new ImageView(inflater.getContext());
        } else {
            imageView=(ImageView)view;
        }

        Picasso.with(inflater.getContext()).load(mImageList.get(i).toString()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}
