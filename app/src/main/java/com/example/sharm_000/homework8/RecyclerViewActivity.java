package com.example.sharm_000.homework8;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

public class RecyclerViewActivity extends AppCompatActivity implements RecyclerViewFragment.OnCardItemClickedListener{

    Fragment recyclerFragment;
    Toolbar recyclerToolBar;
    ActionBar recyclerActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerToolBar = (Toolbar) findViewById(R.id.toolBarRecycler);
        setSupportActionBar(recyclerToolBar);
        recyclerToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerActionBar = getSupportActionBar();
        recyclerActionBar.setDisplayHomeAsUpEnabled(true);
        recyclerActionBar.setHomeButtonEnabled(true);

        if(savedInstanceState!=null){
            if(getSupportFragmentManager().getFragment(savedInstanceState,"recyclerFrag") != null){
                recyclerFragment = getSupportFragmentManager().getFragment(savedInstanceState,"recyclerFrag");
            }else
                recyclerFragment = RecyclerViewFragment.newInstance();
        }else
            recyclerFragment = RecyclerViewFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewAct, recyclerFragment).commit();
    }

    @Override
    public void onCardItemClicked(HashMap<String,?> movie) {
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewAct,MovieFragment.newInstance(movie)).addToBackStack(null).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recyclerFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "recyclerFrag", recyclerFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
