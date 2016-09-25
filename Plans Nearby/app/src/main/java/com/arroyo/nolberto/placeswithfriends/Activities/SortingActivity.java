package com.arroyo.nolberto.placeswithfriends.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arroyo.nolberto.placeswithfriends.Constants;
import com.arroyo.nolberto.placeswithfriends.Fragments.AboutFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.EventsFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.VenuesFragment;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.R;

/**
 * activity receives nav drawer selections, and host fragments selected in nav drawer
 * trending, coffee, and about fragments
 */
public class SortingActivity extends AppCompatActivity implements ItemClickInterface{
    private VenuesFragment venuesFragment;
    private EventsFragment weekendEvents;
    private FragmentTransaction transaction;
    private AboutFragment aboutFragment;
    private String city,section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        receiveCategoryFromMain();
        startCategoryFragment();

    }
    //gets category and city from MainActivity intent
    public void receiveCategoryFromMain() {
        Intent intent = getIntent();
        this.city = intent.getStringExtra(Constants.MAIN_DRAWER_CITY_KEY);
        this.section = intent.getStringExtra(Constants.MAIN_DRAWER_CATEGORY_KEY);

    }
    //checking which fragment to start using the category received from MainActivity
    public void startCategoryFragment(){

        if (section.equalsIgnoreCase(Constants.MAIN_CATEGORY_TRENDING)|| section.equalsIgnoreCase(Constants.MAIN_CATEGORY_COFFEE)) {
            venuesFragment = new VenuesFragment();
            venuesFragment.setValues(city, section);
            launchFragment(venuesFragment);
        }else if (section.equalsIgnoreCase(Constants.MAIN_CATEGORY_WEEKEND)){
            weekendEvents = new EventsFragment();
            weekendEvents.setValues(city,section);
            launchFragment(weekendEvents);
        }else {
            aboutFragment = new AboutFragment();
            launchFragment(aboutFragment);
        }

    }

    public void launchFragment (Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.sorting_container, fragment)
                .commit();

    }

    //fragments detached
    @Override
    protected void onPause() {
        super.onPause();
        transaction.detach(venuesFragment);
        transaction.detach(weekendEvents);
        this.city=null;
        this.section=null;
    }

    @Override
    public void onItemClicked(String selectedItem) {

    }
}
