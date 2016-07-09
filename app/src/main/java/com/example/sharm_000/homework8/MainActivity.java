package com.example.sharm_000.homework8;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
 HomeFragment.OnAboutMeClickedListener, HomeFragment.OnTask2ClickedListener, HomeFragment.OnTask3ClickedListener{
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolBar;
    ActionBar homeActionBar;

    /*public interface OnBackPressedListener {
        void onBackPressed();
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar= (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.navDrawer);
        ActionBarDrawerToggle toggler = new ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(toggler);

        /*toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        homeActionBar = getSupportActionBar();
        homeActionBar.setDisplayHomeAsUpEnabled(true);
        homeActionBar.setHomeButtonEnabled(true);
        toggler.syncState();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.mainContainer,HomeFragment.newInstance()).commit();
        }
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //boolean drawerOpen = drawerLayout.isDrawerOpen(navigationView);
        //menu.findItem(R.id.)
        return super.onPrepareOptionsMenu(menu);
    }*/



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.aboutMe:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, AboutMeFragment.newInstance()).addToBackStack(null).commit();
                break;
            case R.id.task2:
                Intent intent = new Intent(this, RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.task3:
                Intent intent1 = new Intent(this, RecyclerViewActivity1.class);
                startActivity(intent1);
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, HomeFragment.newInstance()).addToBackStack(null).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAboutMeClicked(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, AboutMeFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onTask2Clicked(Bundle savedInstanceState) {
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTask3Clicked(Bundle savedInstanceState) {
        Intent intent1 = new Intent(this, RecyclerViewActivity1.class);
        startActivity(intent1);
    }
}
