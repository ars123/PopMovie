package com.example.user.app.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public MovieAdapter mMovieAdapter;
    ArrayList<Movie> mMovies = new ArrayList<Movie>();
    Bundle bundle=new Bundle();
    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
  //  private static final String FAVORITE = "favorite";
    private static final String MOVIES_KEY = "movies";

    private String mSortMovieBy = POPULARITY_DESC;

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);

        MenuItem sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem sort_by_rating = menu.findItem(R.id.action_sort_by_rating);

        if (mSortMovieBy.contentEquals(POPULARITY_DESC)) {
            if (!sort_by_popularity.isChecked()) {
                sort_by_popularity.setChecked(true);
            }
        } else if (mSortMovieBy.contentEquals(RATING_DESC)) {
            if (!sort_by_rating.isChecked()) {
                sort_by_rating.setChecked(true);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortMovieBy = POPULARITY_DESC;
                updateMovies(mSortMovieBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortMovieBy = RATING_DESC;
                updateMovies(mSortMovieBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateMovies(String sort_by){
       new FetchMovieTask().execute(sort_by);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView)rootView.findViewById(R.id.movies_grid);
        mMovieAdapter = new MovieAdapter(getActivity(),mMovies);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie currentMovie = mMovieAdapter.getItem(i);
                Intent movieDetailIntent = new Intent(getActivity(),DetailActivity.class);
                movieDetailIntent.putExtra("currentMovie",currentMovie);
                startActivity(movieDetailIntent);


            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                mSortMovieBy = savedInstanceState.getString(SORT_SETTING_KEY);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mMovieAdapter.setData(mMovies);
            } else {
                updateMovies(mSortMovieBy);
            }
        } else {
            updateMovies(mSortMovieBy);
        }
        return rootView;
    }
   /* @Override
    public void onStart() {
        super.onStart();
        updateMovies(mSortMovieBy);
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!mSortMovieBy.contentEquals(POPULARITY_DESC)) {
            outState.putString(SORT_SETTING_KEY, mSortMovieBy);
        }
        if (mMovies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        }
        super.onSaveInstanceState(outState);
    }


    public class FetchMovieTask extends AsyncTask<String, Void,ArrayList<Movie>>{

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException{
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            ArrayList<Movie> results = new ArrayList<>();

            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movie movieModel = new Movie(movie);
                results.add(movieModel);
            }

            return results;
        }


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if(params.length == 0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            String apiKey = "ENTER YOUR API KEY HERE";

            try{

                //For building the URL for the movies db api  http://api.themoviedb.org/3/discover/movie?sort_by=popularity_desc&api_key=
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM,params[0])
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
            Log.v("MY_TAG", "movies result array is " + result.size());
            if (result != null) {
                if (mMovieAdapter != null) {
                    mMovieAdapter.setData(result);
                }
                mMovies.addAll(result);
            }

            mMovieAdapter.notifyDataSetChanged();
        }
    }
}


