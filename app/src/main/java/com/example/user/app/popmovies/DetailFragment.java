package com.example.user.app.popmovies;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    Movie movie;
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ScrollView mMovieDetailContainer = (ScrollView)rootView.findViewById(R.id.movie_detail_container);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.imageThumbnail);
        TextView titleView = (TextView)rootView.findViewById(R.id.titleView);
        TextView overviewView = (TextView)rootView.findViewById(R.id.overView);
        TextView votingView= (TextView)rootView.findViewById(R.id.voteView);
        TextView releaseDateView = (TextView)rootView.findViewById(R.id.dateView);

        Bundle bundle=getArguments();

        if(bundle!=null){
            movie = bundle.getParcelable(DETAIL_MOVIE);

            if(movie!=null){
                mMovieDetailContainer.setVisibility(rootView.VISIBLE);
            }
            else{
                mMovieDetailContainer.setVisibility(rootView.INVISIBLE);
            }

            if(movie!=null) {

                Log.v("######", "received title is " + movie.getTitle());
                Log.v("######", "received overview is " + movie.getOverview());
                Log.v("######", "received voting is " + movie.getRating());
                Log.v("######", "received releaseDate is " + movie.getDate());

                String imageUrl = Utility.buildImageUrl(342,movie.getBackDropImage());
                Picasso.with(getActivity()).load(imageUrl).into(imageView);

                titleView.setText(movie.getTitle());
                overviewView.setText(movie.getOverview());
                votingView.setText(String.valueOf(movie.getRating()));
                releaseDateView.setText(movie.getDate());
            }
        }
        return rootView;
    }
}



