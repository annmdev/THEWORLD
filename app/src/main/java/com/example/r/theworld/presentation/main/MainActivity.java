package com.example.r.theworld.presentation.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.favorites.FavoritesListFragment;
import com.example.r.theworld.presentation.map.HomeFragment;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FAVORITES_FRAG = "favorites_frag";
    private static final String TAG_HOME_FRAG = "home_frag";

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.frag_container);

        bottomNavigationView = findViewById(R.id.bottom_nv);
        setOnBottomNavViewClickListener();

        if (fragment == null) {
            fragment = HomeFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.frag_container, fragment)
                    .addToBackStack(TAG_HOME_FRAG)
                    .commit();
        }
    }

    private void setOnBottomNavViewClickListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    case R.id.favorites:
                        if (!(fragment instanceof FavoritesListFragment)) {
                            replaceFragment(FavoritesListFragment.newInstance(), TAG_FAVORITES_FRAG);
                        }
                        break;
                    case R.id.map:
                        if (!(fragment instanceof HomeFragment)) {
                            replaceFragment(HomeFragment.newInstance(), TAG_HOME_FRAG);

                        }
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        this.fragment = fragment;
        fm.beginTransaction()
                .replace(R.id.frag_container, fragment)
                .addToBackStack(tag)
                .commit();
    }
}
