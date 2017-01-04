package com.example.user.app.popmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public MovieAdapter mMovieAdapter;
    ArrayList<Movie> mMovies = new ArrayList<Movie>();
    Bundle bundle=new Bundle();
    /* Variables for movie details */
    private static String movieTitle;
    private static int movieId;
    private static String moviePosterPath;
    private static String movieOverview;
    private static double movieVoteCount;
    private static String movieOriginalTitle;
    private static double movieVoteAverage;
    private static double moviePopularity;
    private static String movieBackdropPath;
    private static String movieReleaseDate;


    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView)rootView.findViewById(R.id.movies_grid);
        mMovieAdapter = new MovieAdapter(getActivity(),new ArrayList<Movie>());
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie currentMovie = mMovieAdapter.getItem(i);
                Intent movieDetailIntent = new Intent(getActivity(),DetailActivity.class);
                movieDetailIntent.putExtra("title", currentMovie.getMovieOriginalTitle());
                movieDetailIntent.putExtra("overview",currentMovie.getMovieOverview());
                movieDetailIntent.putExtra("voting",currentMovie.getMovieVoteAverage());
                movieDetailIntent.putExtra("releaseDate",currentMovie.getMovieReleaseDate());
                startActivity(movieDetailIntent);


            }
        });
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        FetchMovieTask movieData = new FetchMovieTask();
        movieData.execute();
    }


    public class FetchMovieTask extends AsyncTask<Void, Void,ArrayList<Movie>>{

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException{
            // Create a movie reference
            Movie movie;
            // Parse the jsonResponse string
            JSONObject movieJsonResponse = new JSONObject(movieJsonStr);
            if (movieJsonResponse.has("results")) {
                JSONArray resultsArray = movieJsonResponse.getJSONArray("results");
                if (resultsArray.length() > 0) {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject movieDetail = resultsArray.getJSONObject(i);
                        if (movieDetail.has("title")) {
                            movieTitle = movieDetail.getString("title");
                        }
                        if (movieDetail.has("id")) {
                            movieId = movieDetail.getInt("id");
                        }
                        if (movieDetail.has("poster_path")) {
                            moviePosterPath = movieDetail.getString("poster_path");
                            Log.v("poster path",moviePosterPath);
                        }
                        if (movieDetail.has("overview")) {
                            movieOverview = movieDetail.getString("overview");
                        }
                        if (movieDetail.has("original_title")) {
                            movieOriginalTitle = movieDetail.getString("original_title");
                            Log.v("Movie Title:",movieOriginalTitle);
                        }
                        if (movieDetail.has("backdrop_path")) {
                            movieBackdropPath = movieDetail.getString("backdrop_path");
                        }
                        if (movieDetail.has("popularity")) {
                            moviePopularity = movieDetail.getDouble("popularity");
                        }
                        if (movieDetail.has("vote_count")) {
                            movieVoteCount = movieDetail.getDouble("vote_count");
                        }
                        if (movieDetail.has("vote_average")) {
                            movieVoteAverage = movieDetail.getDouble("vote_average");
                        }
                        if (movieDetail.has("release_date")) {
                            movieReleaseDate = movieDetail.getString("release_date");
                        }

                    }
                }
            }
            // Return the list of Movies
            return mMovies;
        }


        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            String sort_by = "popularity.desc";
            String apiKey = "e04f08387e30e9ba46f930a31e0d69fd";

            try{

                //For building the URL for the movies db api  http://api.themoviedb.org/3/discover/movie?sort_by=popularity_desc&api_key=
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sort_by)
                        .appendQueryParameter(API_PARAM,apiKey).build();



                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI" + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0){
                    return null;
                }

                movieJsonStr = buffer.toString();

            } catch (IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);
            if(result != null){
                for (int i=0; i<result.size();i++){
                    mMovies.add(new Movie(movieTitle, movieId, moviePosterPath, movieOverview, movieVoteCount,
                            movieOriginalTitle, movieVoteAverage, moviePopularity, movieBackdropPath, movieReleaseDate));
                }
            }
            mMovieAdapter.notifyDataSetChanged();
        }
    }

 }


