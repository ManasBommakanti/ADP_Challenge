package com.example.appchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    //private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private int[] tabIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabIcons = new int[]{
                R.drawable.ic_add,
                R.drawable.ic_history,
                R.drawable.ic_settings};

        getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));

        toolbar = findViewById(R.id.id_toolBar);
        setSupportActionBar(toolbar);

     // viewPager = findViewById(R.id.id_pager);
    //  viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // viewPager.setAdapter(viewPagerAdapter);

        tabLayout = findViewById(R.id.id_tabs);

        tabLayout.setTabIndicatorFullWidth(true);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AddEventFragment newFrag = new AddEventFragment();
        transaction.add(R.id.id_pager, newFrag);
        transaction.commit();

        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[2]));
        tabLayout.getTabAt(0).setText("New Entry");
        tabLayout.getTabAt(1).setText("Past Entries");
        tabLayout.getTabAt(2).setText("Settings");
        Log.d("TABS", ""+tabLayout.getTabCount());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition())
                {
                    case 0:
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        AddEventFragment newFrag = new AddEventFragment();
                        transaction.replace(R.id.id_pager, newFrag);
                        transaction.commit();
                        break;

                    case 1:
                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction transaction2 = manager2.beginTransaction();
                        PastEntriesFragment newFrag2 = new PastEntriesFragment();
                        transaction2.replace(R.id.id_pager, newFrag2);
                        transaction2.commit();
                        break;

                    case 2:
                        FragmentManager manager3 = getSupportFragmentManager();
                        FragmentTransaction transaction3 = manager3.beginTransaction();
                        SettingFragment newFrag3 = new SettingFragment();
                        transaction3.replace(R.id.id_pager, newFrag3);
                        transaction3.commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public View LoggerAction(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        AddEventFragment fragment = new AddEventFragment();
        return fragment.onCreateView(inflater, container, savedInstanceState);
    }
    public View PastEntriesAction(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        PastEntriesFragment fragment = new PastEntriesFragment();
        return fragment.onCreateView(inflater, container, savedInstanceState);
    }
    public View SettingsAction(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SettingFragment fragment = new SettingFragment();
        return fragment.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
      
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        // Hide the nav bar and status bar
                        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        //| View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
