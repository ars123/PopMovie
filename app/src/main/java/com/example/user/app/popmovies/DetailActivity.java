package com.example.user.app.popmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**get the movie's Object from the parent activity**/
        Movie movie = getIntent().getParcelableExtra("currentMovie");


       /* put the movie into the parsel*/
        Bundle movieDetails = new Bundle();
        movieDetails.putParcelable(DetailFragment.DETAIL_MOVIE, movie);
        //Log.v("######", "received movie is " + movie);

        if(savedInstanceState == null){

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(movieDetails);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container,new DetailFragment()).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public static final String TAG = DetailFragment.class.getSimpleName();

        static final String DETAIL_MOVIE = "DETAIL_MOVIE";
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
                movie = bundle.getParcelable(DETAIL_MOVIE);

                if(movie!=null) {

                    Log.v("######", "received title is " + movie.getTitle());
                    Log.v("######", "received overview is " + movie.getOverview());
                    Log.v("######", "received voting is " + movie.getRating());
                    Log.v("######", "received releaseDate is " + movie.getDate());
                    titleView.setText(movie.getTitle());
                    overviewView.setText(movie.getOverview());
                    votingView.setText(movie.getOverview());
                    releaseDateView.setText(movie.getDate());
                }
            }
            return rootView;
        }
    }


}
