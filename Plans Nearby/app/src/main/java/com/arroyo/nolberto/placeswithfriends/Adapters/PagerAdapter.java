package com.arroyo.nolberto.placeswithfriends.Adapters;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.arroyo.nolberto.placeswithfriends.Fragments.EventsFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.NearByFragment;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    String query;
    EventsFragment tab1;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new EventsFragment();
                tab1.setResultQuery(query);
                Log.d("pager query","result:"+query);
                return tab1;
            case 1:
                //calling setResultQuery to pass query to fragment
                NearByFragment tab2 = new NearByFragment();
                return tab2;
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
