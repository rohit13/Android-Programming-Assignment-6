package com.example.sharm_000.homework8;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    HashMap<String,?> movie;
    TextView movieName;
    ImageView imgView;
    TextView desc;
    RatingBar rating;
    TextView stars;
    TextView textRating;
    TextView dirName;
    ShareActionProvider shareActionProvider;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
       // SetHasOptionsMenu(true);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        movie = (HashMap<String, ?>) getArguments().getSerializable("movie");
        View view= inflater.inflate(R.layout.fragment_movie, container, false);
        if(movie!=null && movie.size()>0) {
            movieName = (TextView) view.findViewById(R.id.movieName);
            movieName.setText((String) movie.get("name") + " (" + (String) movie.get("year") + ")");

            imgView = (ImageView) view.findViewById(R.id.imgView3);
            //imgView.setImageResource((Integer) movie.get("image"));
            imgView.setImageBitmap((Bitmap) movie.get("image"));
            desc = (TextView) view.findViewById(R.id.desc);
            desc.setText((String) movie.get("description"));

            rating = (RatingBar) view.findViewById(R.id.ratingBar);
            //rating.setRating((Float) movie.get("rating"));

            double mRating = (Double) movie.get("rating");
            float convRating = (float) mRating / 2.0f;
            rating.setRating(convRating);

            textRating = (TextView) view.findViewById(R.id.textRating);
            textRating.setText("" + (convRating * 2) + "/10");

            stars = (TextView) view.findViewById(R.id.stars);
            stars.setText((String) movie.get("stars"));

            dirName = (TextView) view.findViewById(R.id.directorName);
            dirName.setText((String) movie.get("director"));
        }
        return view;
    }

    public static MovieFragment newInstance(HashMap<String, ?> item) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie",item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.shareAction)==null){
            inflater.inflate(R.menu.movie_fragment_action_provider,menu);
        }
        MenuItem share = menu.findItem(R.id.shareAction);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);

        //Create intent
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        if(movie!=null && movie.size()>0) {
            intentShare.putExtra(Intent.EXTRA_TEXT, (String) movie.get("name") + movie.get("description"));
        }else{
            intentShare.putExtra(Intent.EXTRA_TEXT, "No movie available for share.");
        }

        //set intent
        shareActionProvider.setShareIntent(intentShare);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
