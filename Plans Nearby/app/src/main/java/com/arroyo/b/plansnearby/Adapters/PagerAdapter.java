package com.arroyo.b.plansnearby.Adapters;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.arroyo.b.plansnearby.Constants;
import com.arroyo.b.plansnearby.Fragments.VenuesFragment;
import com.arroyo.b.plansnearby.Fragments.EventsFragment;

/**
 * Created by nolbertoarroyo on 8/19/16.
 * this adapter class handles information being passed from MainActivity headed to fragments
 * passes,query,city, and fragment categories, which are used in fragments to get api results
 */


public class PagerAdapter extends FragmentStatePagerAdapter {
    private EventsFragment tab1;
    private EventsFragment tab2;
    private VenuesFragment tab3;
    private VenuesFragment tab4;
    private VenuesFragment tab5;
    private VenuesFragment tab6;
    private int mNumOfTabs;
    private String query, city;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    //creates fragments and passed initial properties
    //when view pager is created
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new EventsFragment();
                tab1.setCategoryQuery(query, city, Constants.TAB_INTERESTS_EVENTS);
                return tab1;
            case 1:
                tab2 = new EventsFragment();
                tab2.setCategoryQuery(query, city, Constants.TAB_ALL_EVENTS);
                return tab2;
            case 2:
                tab3 = new VenuesFragment();
                tab3.setCity(city, Constants.TAB_FOOD);
                return tab3;

            case 3:
                tab4 = new VenuesFragment();
                tab4.setCity(city, Constants.TAB_DRINKS);
                return tab4;
            case 4:
                tab5 = new VenuesFragment();
                tab5.setCity(city, Constants.TAB_TOP_PICKS);
                return tab5;
            case 5:
                tab6 = new VenuesFragment();
                tab6.setCity(city, Constants.TAB_SIGHTS);
                return tab6;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


    // method gets values from MainActivity
    //passes information to fragments after they
    //have already been created, (updates fragments)
    public void setQuery(String query, String city) {
        this.query = query;
        this.city = city;


        if (city == null && query == null) {
            tab2.setCategoryQuery(query, city, Constants.TAB_ALL_EVENTS);
            tab1.setCategoryQuery(query, city, Constants.TAB_INTERESTS_EVENTS);
            tab3.setCity(city, Constants.TAB_FOOD);
            tab4.setCity(city, Constants.TAB_DRINKS);
            tab5.setCity(city, Constants.TAB_TOP_PICKS);
            tab6.setCity(city, Constants.TAB_SIGHTS);
        } else if (city != null) {
            tab2.setCategoryQuery(query, city, Constants.TAB_ALL_EVENTS);
            tab1.setCategoryQuery(query, city, Constants.TAB_INTERESTS_EVENTS);
            tab3.setCity(city, Constants.TAB_FOOD);
            tab4.setCity(city, Constants.TAB_DRINKS);
            tab5.setCity(city, Constants.TAB_TOP_PICKS);
            tab6.setCity(city, Constants.TAB_SIGHTS);
        }
    }
}
