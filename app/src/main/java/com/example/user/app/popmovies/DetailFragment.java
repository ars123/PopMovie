package com.example.user.app.popmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    Movie movie;
    public static final String TAG = DetailFragment.class.getSimpleName();

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView titleView = (TextView)rootView.findViewById(R.id.titleView);
        TextView overviewView = (TextView)rootView.findViewById(R.id.overview);
        TextView votingView= (TextView)rootView.findViewById(R.id.voteView);
        TextView releaseDateView = (TextView)rootView.findViewById(R.id.dateView);

        Bundle bundle=getArguments();

        if(bundle!=null){
            movie = bundle.getParcelable(DetailFragment.DETAIL_MOVIE);
            if(movie!=null) {
                titleView.setText(movie.getTitle());
                overviewView.setText(movie.getOverview());
                votingView.setText(movie.getOverview());
                releaseDateView.setText(movie.getDate());
            }
        }
        return rootView;
    }
}
