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

public class RecyclerViewActivity1 extends AppCompatActivity implements RecyclerViewFragment.OnCardItemClickedListener{

    Fragment recyclerFragment;
    Toolbar recyclerToolBar;
    ActionBar recyclerActionBar;
    Toolbar toolBarAsAction;
    //OnToolBarButtonClickedListener onToolBarButtonClickedListener;
    //RecyclerViewFragment fragment;

    /*public interface OnToolBarButtonClickedListener{
        void onDeleteAllButtonClicked();
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_1);

        toolBarAsAction = (Toolbar) findViewById(R.id.toolBarAsAction);
        //setSupportActionBar(recyclerToolBar);
        setSupportActionBar(toolBarAsAction);
        toolBarAsAction.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerActionBar = getSupportActionBar();
        recyclerActionBar.setDisplayHomeAsUpEnabled(true);
        recyclerActionBar.setHomeButtonEnabled(true);

        recyclerToolBar = (Toolbar) findViewById(R.id.toolBarRecycler1);
        recyclerToolBar.inflateMenu(R.menu.recycler_view_menu_1_toolbar);
        recyclerToolBar.setNavigationIcon(R.drawable.close);
        recyclerToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerToolBar.setVisibility(View.GONE);
            }
        });
        recyclerToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    /*case R.id.offButtonToolBar:
                            recyclerToolBar.setVisibility(View.GONE);
                            return true;*/
                    case R.id.deleteAll:
                        Fragment f1 = getSupportFragmentManager().findFragmentById(R.id.recyclerViewAct1);
                        if(f1 instanceof MovieFragment)
                            return false;
                        RecyclerViewFragment fragment = (RecyclerViewFragment)getSupportFragmentManager().findFragmentById(R.id.recyclerViewAct1);
                        if(fragment instanceof RecyclerViewFragment) {
                            fragment.deleteAllMovies();
                            return true;
                        }
                        return true;
                    case R.id.gridView:
                        Fragment f2 = getSupportFragmentManager().findFragmentById(R.id.recyclerViewAct1);
                        if(f2 instanceof MovieFragment)
                            return false;
                        RecyclerViewFragment fragment1 = (RecyclerViewFragment)getSupportFragmentManager().findFragmentById(R.id.recyclerViewAct1);
                        if(fragment1 instanceof RecyclerViewFragment) {
                            fragment1.changeToGridView();
                            return true;
                        }
                    default:
                        return false;
                }
            }
        });


        if(savedInstanceState!=null){
            if(getSupportFragmentManager().getFragment(savedInstanceState,"recyclerFrag") != null){
                recyclerFragment = getSupportFragmentManager().getFragment(savedInstanceState,"recyclerFrag");
            }else
                recyclerFragment = RecyclerViewFragment.newInstance();
        }else
            recyclerFragment = RecyclerViewFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewAct1, recyclerFragment).commit();
    }

    @Override
    public void onCardItemClicked(HashMap<String,?> movie) {
        getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewAct1, MovieFragment.newInstance(movie)).addToBackStack(null).commit();
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
        int id = item.getItemId();
        switch (id){
            case R.id.onButton:
                recyclerToolBar.setVisibility(View.VISIBLE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recycler_view_menu_1, menu);
        return true;
    }
}
