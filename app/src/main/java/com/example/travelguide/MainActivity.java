package com.example.travelguide;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelguide.ui.ExploreFragment;
import com.example.travelguide.ui.HomeFragment;
import com.example.travelguide.ui.PlanFragment;
import com.example.travelguide.ui.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    // Fragment实例
    private HomeFragment homeFragment;
    private ExploreFragment exploreFragment;
    private PlanFragment planFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initFragments();
        setupBottomNavigation();

        // 默认显示首页
        showFragment(homeFragment);
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        exploreFragment = new ExploreFragment();
        planFragment = new PlanFragment();
        settingsFragment = new SettingsFragment();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                showFragment(homeFragment);
                return true;
            } else if (itemId == R.id.nav_explore) {
                showFragment(exploreFragment);
                return true;
            } else if (itemId == R.id.nav_plan) {
                showFragment(planFragment);
                return true;
            } else if (itemId == R.id.nav_settings) {
                showFragment(settingsFragment);
                return true;
            }

            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}