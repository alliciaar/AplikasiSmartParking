package com.alice.parkingapp.Class;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.alice.parkingapp.Fragment.HomeFragment;
import com.alice.parkingapp.Fragment.PesananFragment;
import com.alice.parkingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        //bottomNavigationViewEx.setLargeTextSize(10);
    }


    public static void enableNavigation(final Context context, BottomNavigationViewEx view, String lattitude, String longitude) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int position = 0;

                Menu menu = view.getMenu();
                MenuItem menuItem;

                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.i_home:
                        menu.getItem(0).setChecked(true);
                        menu.getItem(1).setChecked(false);

                        HomeFragment homeFragment = new HomeFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("lattitude", lattitude);
                        bundle.putString("longitude", longitude);

                        homeFragment.setArguments(bundle);
                        selectedFragment = homeFragment;
                        break;
                    case R.id.i_pesanan:
                        menu.getItem(0).setChecked(false);
                        menu.getItem(1).setChecked(true);
                        selectedFragment = new PesananFragment();
                        break;
                }

                if(previousPosition != position) {

                }

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, selectedFragment)
                        .commit();

                return true;
            }
        });
    }
}
