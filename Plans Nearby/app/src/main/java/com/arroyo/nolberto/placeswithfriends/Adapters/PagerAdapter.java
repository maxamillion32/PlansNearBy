package com.arroyo.nolberto.placeswithfriends.Adapters;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.arroyo.nolberto.placeswithfriends.Fragments.VenuesFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.EventsFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.ForYouEventsFragment;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter{
    private int mNumOfTabs;
    private String query;
    private String city;
    private ForYouEventsFragment tab1;
    private EventsFragment tab2;
    private VenuesFragment tab3;
    private VenuesFragment tab4;
    private VenuesFragment tab5;
    private VenuesFragment tab6;
    private String category;



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
                tab3 = new VenuesFragment();
                category = "food";
                tab3.setCity(city,category);
                return tab3;

            case 3:
                tab4 = new VenuesFragment();
                tab4.setCity(city,"drinks");
                return tab4;
            case 4:
                tab5 = new VenuesFragment();
                category = "topPicks";
                tab5.setCity(city,category);
                Log.d("drinks","result:"+city);
                return tab5;
            case 5:
                tab6 = new VenuesFragment();
                category = "sights";
                tab6.setCity(city,category);
                Log.d("drinks","result:"+city);
                return tab6;

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
            tab3.setCity(city,"food");
            tab4.setCity(city,"drinks");
            tab5.setCity(city,"topPicks");
            tab6.setCity(city,"sights");
        }else if (city!= null){
            tab2.setResultQuery(query, city);
        tab1.setCategoryQuery(query, city);
        tab3.setCity(city,"food");
        tab4.setCity(city,"drinks");
        tab5.setCity(city,"topPicks");
        tab6.setCity(city,"sights");
        }
        Log.d("adapter query","result:"+city);
    }
}
