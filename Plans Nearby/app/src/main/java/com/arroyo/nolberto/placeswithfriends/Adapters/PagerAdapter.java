package com.arroyo.nolberto.placeswithfriends.Adapters;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.arroyo.nolberto.placeswithfriends.Fragments.DrinksFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.EventsFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.ForYouEventsFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.FoodFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.NearByFragment;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter{
    private int mNumOfTabs;
    private String query;
    private String city;
    private ForYouEventsFragment tab1;
    private EventsFragment tab2;
    private FoodFragment tab3;
    private DrinksFragment tab4;



    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new ForYouEventsFragment();
                tab1.setCategoryQuery(query,city);
                return tab1;
            case 1:
                //calling setResultQuery to pass query to fragment
                tab2 = new EventsFragment();
                tab2.setResultQuery(query,city);
                Log.d("pager query","result:"+query+ city);
                return tab2;
            case 2:
                tab3 = new FoodFragment();
                tab3.setCity(city);
                return tab3;

            case 3:
                tab4 = new DrinksFragment();
                tab4.setCity(city);
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query, String city) {
        this.query = query;
        this.city = city;
        //code below needed when locaiton icon is pressed
        //without this code the icon doesn't work
        if (city == null && query == null) {
            tab2.setResultQuery(query, city);
            tab1.setCategoryQuery(query, city);
            tab3.setCity(city);
            tab4.setCity(city);
        }else if (city!= null){
            tab2.setResultQuery(query, city);
            tab1.setCategoryQuery(query, city);
            tab3.setCity(city);
            tab4.setCity(city);
        }
        Log.d("adapter query","result:"+query);
    }
}
