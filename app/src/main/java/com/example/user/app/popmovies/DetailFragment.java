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
            movie = getArguments().getParcelable("movie");
            titleView.setText(movie.getMovieTitle());
            overviewView.setText(movie.getMovieOverview());
            votingView.setText((int) movie.getMovieVoteAverage());
            releaseDateView.setText(movie.getMovieReleaseDate());
        }
        return rootView;
    }
}
