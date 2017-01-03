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

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent= getActivity().getIntent();
        Bundle b = intent.getBundleExtra("movie_bundle");
        String title= b.getString("title");
        String overview = b.getString("overview");
        String votingAvg = b.getString("votingAvg");
        String releaseDate = b.getString("releaseDate");
        String thumbnail = b.getString("Thumbnail");
        Uri uri= Uri.parse(thumbnail).buildUpon().build();
        ImageView imageView=(ImageView)rootView.findViewById(R.id.imageThumbnail);

        TextView titleView = (TextView)rootView.findViewById(R.id.titleView);
        TextView overviewView = (TextView)rootView.findViewById(R.id.overview);
        TextView votingView= (TextView)rootView.findViewById(R.id.voteView);
        TextView releaseDateView = (TextView)rootView.findViewById(R.id.dateView);

        titleView.setText(title);
        overviewView.setText(overview);
        votingView.setText(votingAvg);
        releaseDateView.setText(releaseDate);
        imageView.setImageURI(uri);

        return rootView;
    }
}
