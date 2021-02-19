package com.example.flixster;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;

import java.util.List;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.ViewHolder> {
    List<Movie> movieList;
    Context context;

    public movieAdapter(List<Movie> movies,Context context){

        movieList = movies;
        this.context = context;
    }
    @NonNull
    @Override
    //NB: onCreateViewHolder is very expensive expensive as compared to onBindViewHolder because
    //onCreateViewHolder inflates a view. So the adapter calls the onCreateViewHolder a few number of times
    //(as many as will fit the screen) then binds a new View to an old viewHolder whenever new viewHolders are needed

    //involves inflating a layout from XML (into a view) and returning the holder (made from the view)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("movieAdapter","onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.item_movie,parent,false);
        return new ViewHolder(myView);

    }

    @Override
    //involves filling the viewholder with the model data

    //oR populating data into the view through the view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("movieAdapter","onBindViewHolder" + position);
        Movie movie = movieList.get(position);

        holder.bind(movie);
    }

//Get the size of the model data

    //Or total count of items in the list
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //involves matching the components of the viewholder with their equivalent in the layout
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView overview;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            title = itemView.findViewById(R.id.tvTitle);
            overview = itemView.findViewById(R.id.tvOverview);
        }

        public void bind(Movie movie){
            overview.setText(movie.getOverview());
            title.setText(movie.getTitle());
            String imageURL;

            //if screen is in portrait, getposterPath
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                imageURL = movie.getPosterPath();
            }
            else{
                imageURL = movie.getBackdropPath();
            }

            //else if screen is in landscape, get backdropPath

            Glide.with(context).load(imageURL).placeholder(R.drawable.ic_launcher_foreground).into(ivPoster);
        }
    }
}
