package com.example.logintest;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class client_login extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        navView.setSelectedItemId(R.id.home);
    }

    HomeFragment HomeFragment = new HomeFragment();
    orderFragment orderFragment = new orderFragment();
    moneyFragment moneyFragment = new moneyFragment();
    settingFragment settingFragment = new settingFragment();

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, HomeFragment).commit();
                return true;
            case R.id.order:
                findViewById(R.id.box1).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, orderFragment).commit();
                return true;
            case R.id.money:
                findViewById(R.id.box1).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, moneyFragment).commit();
                return true;
            case R.id.setting:
                findViewById(R.id.box1).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, settingFragment).commit();
                return true;
        }
        return false;
    }
    public void changeFragment(String fragment) {
        if(fragment.equals("money"))
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, moneyFragment).commit();
    }

}
