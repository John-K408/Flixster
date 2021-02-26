package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private Object JsonHttpResponseHandler;
    public final String TAG = "MainActivity";
    List<Movie> movies;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        //Create adapter
        final movieAdapter movieAdapter = new movieAdapter(movies,this);


        //set adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //set layout manager for recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                //Debug
                Log.d(TAG,"onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray result = jsonObject.getJSONArray("results");
                    //log information
                    Log.i(TAG,"Results:" + result.toString());
                   movies.addAll(Movie.fromJsonArray(result));
                   movieAdapter.notifyDataSetChanged();
                   Log.i(TAG,"Movies: "+ movies.size());
                } catch (JSONException e) {
                    //log error
                    Log.e(TAG,"Hit json exception",e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG,"onFailure");
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}