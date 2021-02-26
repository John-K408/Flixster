package com.example.flixster;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;
import okhttp3.Headers;


public class DetailActivity extends YouTubeBaseActivity {
    TextView tvOverview;
    TextView tvTitle;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;
    private static final String  YOUTUBE_API_KEY = "AIzaSyDnIcLY1JxFjUKAfxBlMCW9iUPiJezVJic";
    private final String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        youTubePlayerView = binding.player;

        tvOverview = binding.tvOverview;
        tvTitle = binding.tvTitle;
        ratingBar = binding.ratingBar;
        final Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0) return;
                    else{
                        String youtube_key = results.getJSONObject(0).getString("key");
                        intitializeYoutubeKey(youtube_key,movie);
                        Log.d("DetailActivity",youtube_key);
                    }
                } catch (JSONException e) {
                    Log.e("DetailActivity","Failed to parse JSON",e);
                }

            }


            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable){

            }
        });

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());



    }



    private void intitializeYoutubeKey(final String youtube_key, final Movie movie) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity","onInitializationSuccess");
                if(movie.getRating() > 5){
                    youTubePlayer.loadVideo(youtube_key);
                }
                else{
                    youTubePlayer.cueVideo(youtube_key);
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity","onInitializationFailure");
            }
        });
    }


}