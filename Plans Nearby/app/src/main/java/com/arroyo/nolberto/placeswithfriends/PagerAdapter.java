package com.arroyo.nolberto.placeswithfriends;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    String query;
    EventsFragment tab3;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                //calling setResultQuery to pass query to fragment
                SearchFragment tab2 = new SearchFragment();
                return tab2;
            case 2:
                tab3 = new EventsFragment();
                tab3.setResultQuery(query);
                Log.d("pager query","result:"+query);
                return tab3;
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

    public void setQuery(String query) {
        this.query = query;
        Log.d("adapter query","result:"+query);
    }
}
