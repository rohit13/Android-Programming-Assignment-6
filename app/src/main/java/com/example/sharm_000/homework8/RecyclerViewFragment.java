package com.example.sharm_000.homework8;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {
    private static String NO_NETWORK_INFO_MSG = "Check your internet connection!";
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    LinearLayoutManager layoutManager;
    MovieData movieData = new MovieData();
    int layoutType = 1;
    private OnCardItemClickedListener onCardItemClickedListener;
    private MyMoviesGreaterThanRatingAsyncTask ratingTask;
    //LruCache<String,Bitmap> mImgMemoryCache;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        onCardItemClickedListener = (OnCardItemClickedListener) getContext();
        /*if(mImgMemoryCache==null){
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
            final int cacheSize = maxMemory/8;
            mImgMemoryCache = new LruCache<String, Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount()/1024;
                }
            };
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        //layout manager
        if (layoutType == 1)
            layoutManager = new LinearLayoutManager(getActivity());
        else
            layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        //specify adapter

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movieData.getMoviesList(), layoutType);
        recyclerView.setAdapter(recyclerViewAdapter);
        MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(recyclerViewAdapter);
        String url = MovieData.PHP_SERVER + "movies/";
        Log.d("get all movies url: ", url);
        downloadJson.execute(url);
        //adapterAnimation();
        //itemAnimator();


        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HashMap<String, ?> movie = (HashMap<String, ?>) movieData.getItem(position);
                String movieId = "", url;
                if (movie.get("id") != null && !"".equals(movie.get("id"))) {
                    movieId = movie.get("id").toString().trim();
                }
                url = MovieData.PHP_SERVER + "movies/id/" + movieId;
                if (url != null) {
                    MyDownloadMovieDetailsAsyncTask downloadDetailTask = new MyDownloadMovieDetailsAsyncTask(onCardItemClickedListener);
                    downloadDetailTask.execute(url);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                getActivity().startActionMode(new ActionBarCallBack(position));
            }

            @Override
            public void onOverFlowButtonClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.delete:
                                if (!isNetworkAvailable()) {
                                    Snackbar.make(recyclerView, NO_NETWORK_INFO_MSG, Snackbar.LENGTH_SHORT).show();
                                    return false;
                                }
                                HashMap<String, ?> movie = (HashMap<String, ?>) movieData.getItem(position);
                                if (movie != null) {
                                    String movieId = "";
                                    String[] urlArgs = {"", "", ""};
                                    if (movie.get("id") != null && !"".equals(movie.get("id"))) {
                                        movieId = movie.get("id").toString().trim();
                                    }
                                    JSONObject movieJson = new JSONObject();
                                    try {
                                        movieJson.put("id", movieId);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    urlArgs[0] = MovieData.PHP_SERVER + "delete";
                                    urlArgs[1] = movieJson.toString();
                                    urlArgs[2] = String.valueOf(position);
                                    if (urlArgs != null) {
                                        MyDeleteMovieFromDBAsyncTask updateMovieDbTask = new MyDeleteMovieFromDBAsyncTask(recyclerViewAdapter);
                                        updateMovieDbTask.execute(urlArgs);
                                    }
                                }
                                return true;
                            case R.id.duplicate:
                                if (!isNetworkAvailable()) {
                                    Snackbar.make(recyclerView, NO_NETWORK_INFO_MSG, Snackbar.LENGTH_SHORT).show();
                                    return false;
                                }
                                HashMap<String, ?> movie1 = (HashMap<String, ?>) movieData.getItem(position);
                                if (movie1 != null) {
                                    String movieId1 = "", url;
                                    if (movie1.get("id") != null && !"".equals(movie1.get("id"))) {
                                        movieId1 = movie1.get("id").toString().trim();
                                    }
                                    url = MovieData.PHP_SERVER + "movies/id/" + movieId1;
                                    if (url != null) {
                                        MyMovieDetailsForAddAsyncTask downloadNewDetailTask = new MyMovieDetailsForAddAsyncTask(position);
                                        downloadNewDetailTask.execute(url);
                                    }
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater menuInflater = popup.getMenuInflater();
                menuInflater.inflate(R.menu.contextual_action_bar_menu, popup.getMenu());
                popup.show();
            }
        });
        return rootView;
    }

    private void itemAnimator() {
        if (layoutType == 1) {
            SlideInRightAnimator animator = new SlideInRightAnimator();
            animator.setInterpolator(new OvershootInterpolator());
            animator.setAddDuration(500);
            animator.setRemoveDuration(500);
            recyclerView.setItemAnimator(animator);
        } else {
            FadeInAnimator animator = new FadeInAnimator();
            animator.setInterpolator(new OvershootInterpolator());
            animator.setAddDuration(800);
            animator.setRemoveDuration(500);
            recyclerView.setItemAnimator(animator);
        }
    }

    private void adapterAnimation() {
        if (layoutType == 1) {
            ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(recyclerViewAdapter);
            alphaAdapter.setDuration(500);
            recyclerView.setAdapter(alphaAdapter);
        } else {
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(recyclerViewAdapter);
            alphaAdapter.setDuration(500);
            recyclerView.setAdapter(alphaAdapter);
        }

    }

    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnCardItemClickedListener {
        public void onCardItemClicked(HashMap<String, ?> movie);
    }

    public void deleteAllMovies() {
        int size = movieData.getSize();
        for (int i = 0; i < size; i++) {
            movieData.removeItem(i);
            recyclerViewAdapter.notifyItemRemoved(i);
            i--;
            size--;
        }
        if (size <= 5)
            recyclerViewAdapter.notifyDataSetChanged();
    }

    public void changeToGridView() {
        if (layoutType == 1)
            layoutType = 2;
        else if (layoutType == 2)
            layoutType = 1;
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.findItem(R.id.actionSearch) == null) {
            inflater.inflate(R.menu.recycler_view_menu, menu);
        }
        SearchView search = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        if (search != null) {
            search.setSubmitButtonEnabled(true);

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!isNetworkAvailable()) {
                        Snackbar.make(recyclerView, NO_NETWORK_INFO_MSG, Snackbar.LENGTH_SHORT).show();
                        return false;
                    }
                    ratingTask = new MyMoviesGreaterThanRatingAsyncTask(recyclerViewAdapter);
                    String url = MovieData.PHP_SERVER + "movies/rating/" + query.trim();
                    Log.d("movies with rating url:", url);
                    ratingTask.execute(url);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public class ActionBarCallBack implements android.view.ActionMode.Callback {
        int position;

        public ActionBarCallBack(int position) {
            this.position = position;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_action_bar_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            HashMap md = movieData.getItem(position);
            mode.setTitle((String) md.get("name"));
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.delete:
                    movieData.removeItem(position);
                    recyclerViewAdapter.notifyItemRemoved(position);
                    mode.finish();
                    break;
                case R.id.duplicate:
                    movieData.addItem(position + 1, (HashMap<String, ?>) movieData.getItem(position).clone());
                    recyclerViewAdapter.notifyItemInserted(position + 1);
                    mode.finish();
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    private class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, MovieData> {
        private final WeakReference<RecyclerViewAdapter> adapterReference;

        private MyDownloadJsonAsyncTask(RecyclerViewAdapter adapter) {
            adapterReference = new WeakReference<RecyclerViewAdapter>(adapter);
        }

        @Override
        protected MovieData doInBackground(String... urls) {
            MovieData movieDataJson = new MovieData();
            for (String url : urls) {
                movieDataJson.downloadMovieDataJson(url);
            }
            return movieDataJson;
        }

        @Override
        protected void onPostExecute(MovieData movieDataJson) {
            movieData.moviesList.clear();
            for (int m = 0; m < movieDataJson.getSize(); m++) {
                movieData.moviesList.add(movieDataJson.moviesList.get(m));
            }
            if (adapterReference != null) {
                final RecyclerViewAdapter adapter = adapterReference.get();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class MyMoviesGreaterThanRatingAsyncTask extends AsyncTask<String, Void, MovieData> {
        private final WeakReference<RecyclerViewAdapter> adapterWeakReference;

        private MyMoviesGreaterThanRatingAsyncTask(RecyclerViewAdapter adapter) {
            adapterWeakReference = new WeakReference<RecyclerViewAdapter>(adapter);
        }

        @Override
        protected MovieData doInBackground(String... urls) {
            MovieData movieDataRating = new MovieData();
            for (String url : urls) {
                movieDataRating.downloadMovieDataJson(url);
            }
            return movieDataRating;
        }

        @Override
        protected void onPostExecute(MovieData movieDataRating) {
            movieData.moviesList.clear();
            for (int m = 0; m < movieDataRating.getSize(); m++) {
                movieData.moviesList.add(movieDataRating.moviesList.get(m));
            }
            if (adapterWeakReference != null) {
                final RecyclerViewAdapter adapter = adapterWeakReference.get();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class MyDownloadMovieDetailsAsyncTask extends AsyncTask<String, Void, HashMap<String, ?>> {
        private final WeakReference<OnCardItemClickedListener> movieDetailWeakReference;

        private MyDownloadMovieDetailsAsyncTask(OnCardItemClickedListener cardItemClickedListener) {
            movieDetailWeakReference = new WeakReference<OnCardItemClickedListener>(cardItemClickedListener);
        }

        @Override
        protected HashMap<String, ?> doInBackground(String... urls) {
            MovieData movieDataDetails = new MovieData();
            HashMap movieMap = new HashMap();
            for (String url : urls) {
                movieMap = movieDataDetails.downloadMovieDataDetailsJson(url);
            }
            return movieMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, ?> movieMap) {
            if (movieDetailWeakReference != null) {
                final OnCardItemClickedListener listener = movieDetailWeakReference.get();
                if (listener != null) {
                    listener.onCardItemClicked(movieMap);
                }
            }

        }
    }

    private class MyDeleteMovieFromDBAsyncTask extends AsyncTask<String, Void, Integer> {
        private final WeakReference<RecyclerViewAdapter> adapterWeakReference;

        private MyDeleteMovieFromDBAsyncTask(RecyclerViewAdapter adapter) {
            adapterWeakReference = new WeakReference<RecyclerViewAdapter>(adapter);
        }

        @Override
        protected Integer doInBackground(String... urlArgs) {
            if (urlArgs != null) {
                Integer position = Integer.parseInt(urlArgs[2]);
                try {
                    JSONObject movieJSON = new JSONObject(urlArgs[1]);
                    if (MyUtility.sendHttPostRequest(urlArgs[0], movieJSON)) {
                        return position;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer position) {
            if (adapterWeakReference != null) {
                final RecyclerViewAdapter adapter = adapterWeakReference.get();
                if (adapter != null) {
                    if (position != null && position != -1)
                        movieData.removeItem(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        }
    }

    private class MyAddMovieToDBAsyncTask extends AsyncTask<String, Void, Integer> {
        private final WeakReference<RecyclerViewAdapter> adapterWeakReference;
        private HashMap movieMap;

        private MyAddMovieToDBAsyncTask(RecyclerViewAdapter adapter, HashMap<String, ?> map) {
            adapterWeakReference = new WeakReference<RecyclerViewAdapter>(adapter);
            movieMap = new HashMap(map);
        }

        @Override
        protected Integer doInBackground(String... urlArgs) {
            if (urlArgs != null) {
                Integer position = Integer.parseInt(urlArgs[2]);
                try {
                    JSONObject movieJSON = new JSONObject(urlArgs[1]);
                    if (MyUtility.sendHttPostRequest(urlArgs[0], movieJSON)) {
                        movieData.addItem(position + 1, movieMap);
                        return position;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer position) {
            //call MovieData update here again.
            if (adapterWeakReference != null) {
                final RecyclerViewAdapter adapter = adapterWeakReference.get();
                if (adapter != null) {
                    if (position != null && position != -1)
                        adapter.notifyItemInserted(position + 1);
                }
            }
        }
    }

    private class MyMovieDetailsForAddAsyncTask extends AsyncTask<String, Void, HashMap<String, ?>> {
        private int position;

        private MyMovieDetailsForAddAsyncTask(int pos) {
            this.position = pos;
        }

        @Override
        protected HashMap<String, ?> doInBackground(String... urls) {
            MovieData movieDataDetails = new MovieData();
            HashMap movieMap = new HashMap();
            for (String url : urls) {
                movieMap = movieDataDetails.downloadMovieDataDetailsJson(url);
            }
            return movieMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, ?> movieMap) {
            if (movieData != null && movieData.moviesList != null) {
                movieData.moviesList.set(position, movieMap);
                HashMap movieNew = movieData.getItem(position);
                String movieIdNew = "";
                String[] urlArgsNew = {"", "", ""};
                if (movieNew != null) {
                    if (movieNew.get("id") != null && !"".equals(movieNew.get("id"))) {
                        Date timestamp = new Date();
                        if (movieNew.get("id").toString().contains("_")) {
                            String[] movieNameParts = movieNew.get("id").toString().split("_");
                            movieIdNew = movieNameParts[0] + "_" + timestamp.getTime();
                        } else {
                            movieIdNew = movieNew.get("id").toString().trim() + "_" + timestamp.getTime();
                        }
                    }
                    JSONObject movieJsonNew = new JSONObject();
                    try {
                        movieJsonNew.put("id", movieIdNew);
                        movieJsonNew.put("name", movieNew.get("name"));
                        String desc = movieNew.get("description").toString();
                        desc = desc.replaceAll("u0027", "'");
                        movieJsonNew.put("description", desc);
                        movieJsonNew.put("stars", movieNew.get("stars"));
                        movieJsonNew.put("length", movieNew.get("length"));
                        movieJsonNew.put("image", movieNew.get("image"));
                        movieJsonNew.put("year", movieNew.get("year"));
                        movieJsonNew.put("rating", movieNew.get("rating"));
                        movieJsonNew.put("director", movieNew.get("director"));
                        movieJsonNew.put("url", movieNew.get("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HashMap movieNew1 = new HashMap(movieNew);
                    movieNew1.put("id", movieIdNew);
                    urlArgsNew[0] = MovieData.PHP_SERVER + "add";
                    urlArgsNew[1] = movieJsonNew.toString();
                    urlArgsNew[2] = String.valueOf(position);
                    if (urlArgsNew != null) {
                        MyAddMovieToDBAsyncTask addMovieDbTask = new MyAddMovieToDBAsyncTask(recyclerViewAdapter, movieNew1);
                        addMovieDbTask.execute(urlArgsNew);
                    }
                }
            }
        }
    }
}



