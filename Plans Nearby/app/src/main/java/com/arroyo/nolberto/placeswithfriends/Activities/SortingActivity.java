package com.arroyo.nolberto.placeswithfriends.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arroyo.nolberto.placeswithfriends.Fragments.VenuesFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.EventsFragment;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.R;

public class SortingActivity extends AppCompatActivity implements ItemClickInterface{
    private String city;
    private String section;
    VenuesFragment venuesFragment;
    EventsFragment weekendEvents;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        recieveEventSelectedId();
        startCategoryFragment();

    }
    public void recieveEventSelectedId() {
        Intent intent = getIntent();
        this.city = intent.getStringExtra("city");
        this.section = intent.getStringExtra("category");
        Log.i("check"," "+section);

    }
    public void startCategoryFragment(){

        if (section.equalsIgnoreCase("trending")|| section.equalsIgnoreCase("coffee")) {
            venuesFragment = new VenuesFragment();
            venuesFragment.setValues(city, section);
            launchFragment(venuesFragment);
        }else{
            weekendEvents = new EventsFragment();
            weekendEvents.setValues(city,section);
            launchFragment(weekendEvents);

        }

    }
    public void launchFragment (Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.sorting_container, fragment)
                .commit();

    }

    @Override
    public void onItemClicked(String selectedItem) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        transaction.detach(venuesFragment);
        transaction.detach(weekendEvents);
        this.city=null;
        this.section=null;
    }
}
