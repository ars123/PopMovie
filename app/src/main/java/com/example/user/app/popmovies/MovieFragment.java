package com.example.user.app.popmovies;

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
import android.widget.Toast;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public MovieAdapter mPopulateMovie;
    List<Uri> posterURLs = new ArrayList<Uri>();

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
        mPopulateMovie = new MovieAdapter(inflater,posterURLs);
        gridView.setAdapter(mPopulateMovie);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "" + i,
                        Toast.LENGTH_SHORT).show();
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


    public class FetchMovieTask extends AsyncTask<Void, Void, String[]>{

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        /**
         * Prepare image URL for presentation.
         */
        private String formatURL(String relativeURL) {
            String imageBaseURL = "http://image.tmdb.org/t/p/";
            String size = "w185";
            relativeURL = relativeURL.substring(1);
            Uri uri = Uri.parse(imageBaseURL).buildUpon()
                    .appendPath(size)
                    .appendPath(relativeURL).build();
            return uri.toString();
        }


        private String[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String RESULT_LIST = "results";
            final String TITLE = "original_title";  // show in detail activity
            final String POSTER_URL = "poster_path";
            final String OVERVIEW = "overview";  // show in detail activity
            final String POPULARITY = "popularity";
            final String RATING = "vote_average";  //show in detail activity
            final String RELEASE_DATE = "release_date";  // show in detail activity

            JSONObject allMovieData = new JSONObject(movieJsonStr);
            JSONArray resultsArray = allMovieData.getJSONArray(RESULT_LIST);

            String[] posterPaths = new String[resultsArray.length()];
            String[] title = new String[resultsArray.length()];
            String[] overview = new String[resultsArray.length()];
            String[] popularity = new String[resultsArray.length()];
            String[] rating = new String[resultsArray.length()];
            String[] releaseDate = new String[resultsArray.length()];

            for(int i = 0; i < resultsArray.length(); i++) {

                // Get the JSON object representing one movie's details
                JSONObject eachMovie = resultsArray.getJSONObject(i);

                title[i] = eachMovie.getString(TITLE);
                String relativeURL = eachMovie.getString(POSTER_URL);
                posterPaths[i] = formatURL(relativeURL);
                overview[i] = eachMovie.getString(OVERVIEW);
                popularity[i] = eachMovie.getString(POPULARITY);
                rating[i] = eachMovie.getString(RATING);
                releaseDate[i] = eachMovie.getString(RELEASE_DATE);
                Log.v("poster path", posterPaths[i]);
                Log.v("release date",releaseDate[i]);
                Log.v("vote average",rating[i]);
                Log.v("original title",title[i]);
            }

            return posterPaths;

        }

        @Override
        protected String[] doInBackground(Void... params) {

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
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if(result != null){
                for (int i=0; i<result.length;i++){
                    Uri uri = Uri.parse(result[i]);
                    posterURLs.add(uri);
                }
            }
        }
    }


}
