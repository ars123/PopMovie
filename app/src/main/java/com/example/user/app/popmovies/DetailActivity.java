package com.example.user.app.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);*/

        /**get the movie's Object from the parent activity**/
        Movie movie = getIntent().getParcelableExtra("currentMovie");


       /* put the movie into the parsel*/
        Bundle movieDetails = new Bundle();
        movieDetails.putParcelable(DetailFragment.DETAIL_MOVIE, movie);
        Log.v("######", "received released date is " + movie.getDate());

        if (savedInstanceState == null) {

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(movieDetails);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, fragment).commit();
        }

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}

