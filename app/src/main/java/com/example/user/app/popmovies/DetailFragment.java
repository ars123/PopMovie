package com.example.user.app.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

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
public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";
//    static final String DETAIL_TRAILER = "DETAIL_TRAILER";
//    static final String DETAIL_REVIEW = "DETAIL_TRAILER";

    private ShareActionProvider mShareActionProvider;

    private CardView mReviewsCardview;
    private CardView mTrailersCardview;
    private CardView mCastCardView;

    private LinearListView mTrailersView;
    private LinearListView mReviewsView;
    private LinearListView mCastView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private CastAdapter mCastAdapter;

    Trailer mTrailer;
    Movie movie;
    public DetailFragment() {
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(movie!=null){
            inflater.inflate(R.menu.menu_detail,menu);
            final MenuItem action_share = menu.findItem(R.id.action_share);

            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            if (mTrailer != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_detail_title_image_view_backdrop);

        NestedScrollView mMovieDetailContainer = (NestedScrollView) rootView.findViewById(R.id.movie_detail_container);


        ImageView imageView1 = (ImageView)rootView.findViewById(R.id.imageThumbnail);
        TextView titleView = (TextView)rootView.findViewById(R.id.titleView);
        TextView overviewView = (TextView)rootView.findViewById(R.id.overView);
       // TextView votingView= (TextView)rootView.findViewById(R.id.voteView);
        TextView releaseDateView = (TextView)rootView.findViewById(R.id.dateView);

        mTrailersView = (LinearListView) rootView.findViewById(R.id.detail_trailers);
        mReviewsView = (LinearListView) rootView.findViewById(R.id.detail_reviews);
        mCastView = (LinearListView)rootView.findViewById(R.id.detail_cast);

        mReviewsCardview = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);
        mTrailersCardview = (CardView) rootView.findViewById(R.id.detail_trailers_cardview);
        mCastCardView = (CardView)rootView.findViewById(R.id.detail_starcast_cardview);

        /*setting the ratingbar from @link: https://github.com/FlyingPumba/SimpleRatingBar*/
        SimpleRatingBar simpleRatingBar = (SimpleRatingBar) rootView.findViewById(R.id.movieRating);



        Bundle bundle=getArguments();

        if(bundle!=null){
            movie = bundle.getParcelable(DETAIL_MOVIE);

            if(movie!=null){
                mMovieDetailContainer.setVisibility(rootView.VISIBLE);
            }
            else{
                mMovieDetailContainer.setVisibility(rootView.INVISIBLE);
            }

            mTrailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<Trailer>());
            mTrailersView.setAdapter(mTrailerAdapter);

            mTrailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView linearListView, View view,
                                        int position, long id) {
                    Trailer trailer = mTrailerAdapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                    startActivity(intent);
                }
            });

            mReviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<Review>());
            mReviewsView.setAdapter(mReviewAdapter);

            mCastAdapter = new CastAdapter(getActivity(),new ArrayList<Credits>());
            mCastView.setAdapter(mCastAdapter);

            if(movie!=null) {

                Log.v("######", "received title is " + movie.getTitle());
                Log.v("######", "received overview is " + movie.getOverview());
                Log.v("######", "received voting is " + movie.getRating());
                Log.v("######", "received releaseDate is " + movie.getDate());

                String imageUrl = Utility.buildImageUrl(342,movie.getBackDropImage());
                Picasso.with(getActivity()).load(imageUrl).into(imageView);

                String imageUrl1 = Utility.buildImageUrl(185,movie.getBackDropImage());
                Picasso.with(getActivity()).load(imageUrl1).into(imageView1);


                titleView.setText(movie.getTitle());
                overviewView.setText(movie.getOverview());
               // votingView.setText(String.valueOf(movie.getRating()));
                releaseDateView.setText(movie.getDate());
                simpleRatingBar.setRating((float)(movie.getRating() / 2));

            }
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movie != null) {
            new FetchTrailersTask().execute(Integer.toString(movie.getId()));
            new FetchReviewsTask().execute(Integer.toString(movie.getId()));
            new FetchCreditTask().execute(Integer.toString(movie.getId()));
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movie.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> results = new ArrayList<>();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                }
            }

            return results;
        }

        @Override
        protected List<Trailer> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";
                final String apiKey="e04f08387e30e9ba46f930a31e0d69fd";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM,apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    mTrailersCardview.setVisibility(View.VISIBLE);
                    if (mTrailerAdapter != null) {
                        mTrailerAdapter.clear();
                        for (Trailer trailer : trailers) {
                            mTrailerAdapter.add(trailer);
                        }
                    }

                    mTrailer = trailers.get(0);
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareMovieIntent());
                    }
                }
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            List<Review> results = new ArrayList<>();

            for(int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                results.add(new Review(review));
            }

            return results;
        }

        @Override
        protected List<Review> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";
                final String apiKey="e04f08387e30e9ba46f930a31e0d69fd";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM,apiKey )
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                if (reviews.size() > 0) {
                    mReviewsCardview.setVisibility(View.VISIBLE);
                    if (mReviewAdapter != null) {
                        mReviewAdapter.clear();
                        for (Review review : reviews) {
                            mReviewAdapter.add(review);
                        }
                    }
                }
            }
        }
    }



    public class FetchCreditTask extends AsyncTask<String, Void, List<Credits>> {

        private final String LOG_TAG = FetchCreditTask.class.getSimpleName();

        private List<Credits> getCreditsDataFromJson(String jsonStr) throws JSONException {
            JSONObject creditJson = new JSONObject(jsonStr);
            JSONArray creditArray = creditJson.getJSONArray("cast");

            List<Credits> results = new ArrayList<>();

            for(int i = 0; i < creditArray.length(); i++) {
                JSONObject credit = creditArray.getJSONObject(i);
                results.add(new Credits(credit));
            }



            return results;
        }

        @Override
        protected List<Credits> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/credits";
                final String API_KEY_PARAM = "api_key";
                final String apiKey="e04f08387e30e9ba46f930a31e0d69fd";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM,apiKey )
                        .build();

                URL url = new URL(builtUri.toString());

                Log.e(LOG_TAG,url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getCreditsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Credits> credits) {
            super.onPostExecute(credits);
            Log.v("MY_TAG", "credits result array is " + credits +"\n"+ credits.size());
            if (credits != null) {
                if (credits.size() > 0) {
                    mCastCardView.setVisibility(View.VISIBLE);
                    if (mCastAdapter != null) {
                        mCastAdapter.clear();
                        for (Credits credit : credits) {
                            mCastAdapter.add(credit);
                        }
                    }
                }
            }
        }
    }
}



