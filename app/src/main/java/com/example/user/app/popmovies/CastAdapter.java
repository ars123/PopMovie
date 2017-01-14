package com.example.user.app.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User on 14-01-2017.
 */
public class CastAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final Credits mLock = new Credits();
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    private List<Credits> mObjects;

    public CastAdapter(Context context, List<Credits> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Credits object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Credits getItem(int i) {
        return mObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.item_movie_cast, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Credits credits = getItem(i);

        viewHolder = (ViewHolder) view.getTag();

        /*
        Set item views based on your views and data model
        */
        String url = BASE_IMAGE_URL + credits.getMovieCastProfilePath();
        viewHolder.movieCastName.setText(credits.getMovieCastName());
        viewHolder.movieCastCharacter.setText(credits.getMovieCastCharacter());
        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.castplaceholder)
                .into(viewHolder.movieCastImageView);
        return view;

    }

    public static class ViewHolder {
        public final TextView movieCastName;
        public final TextView movieCastCharacter;
        public final ImageView movieCastImageView;

        public ViewHolder(View view) {
            movieCastImageView = (ImageView) view.findViewById(R.id.castImageView);
            movieCastName = (TextView) view.findViewById(R.id.castName);
            movieCastCharacter = (TextView)view.findViewById(R.id.castCharacter);
        }
    }
}
