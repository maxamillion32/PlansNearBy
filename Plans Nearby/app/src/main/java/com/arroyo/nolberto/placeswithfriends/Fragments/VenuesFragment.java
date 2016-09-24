package com.arroyo.nolberto.placeswithfriends.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Activities.MainActivity;
import com.arroyo.nolberto.placeswithfriends.Adapters.CustomRecyclerViewVenuesAdapter;
import com.arroyo.nolberto.placeswithfriends.Constants;
import com.arroyo.nolberto.placeswithfriends.Interfaces.FourSquareServiceInterface;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.CallBackResult;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.Item;
import com.arroyo.nolberto.placeswithfriends.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nolbertoarroyo on 9/12/16.
 */
public class VenuesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private SwipeRefreshLayout onSwipeRefresh;
    private View root;
    private FourSquareServiceInterface fourSquareServiceInterface;
    private ItemClickInterface onItemClickedListener;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private Location location;
    private Call<CallBackResult> call;
    private ArrayList<Item> venues;
    private String currentLocation, section, city;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemClickedListener = (ItemClickInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement interface  ");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_venues, container, false);
        setRecyclerView();
        connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        getLocationFromMain();
        getLocation(location);
        getVenuesList();
        setOnSwipeRefresh();

        return root;
    }

    public void setRecyclerView() {
        onSwipeRefresh = (SwipeRefreshLayout) root.findViewById(R.id.venues_swipe_refresh_layout);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rvLayoutManager);
    }


    /*
     * method runs foursquare interface to get venues by city or by current
     * location depending on which GET method is selected in selectCallMethod()
     * recyclerView is populated with results and displayed, else if city is not null,
     * recycler view is populated with result from city callback on foursquare
     */
    public void getVenuesList() {

        if (networkInfo == null || !networkInfo.isConnected()){
            return;
        }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.FOURSQUARE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            fourSquareServiceInterface = retrofit.create(FourSquareServiceInterface.class);
            // selecting interface method if city is null or not

            selectCallMethod();
        onSwipeRefresh.setRefreshing(true);

            call.enqueue(new Callback<CallBackResult>() {
                @Override
                public void onResponse(Call<CallBackResult> call, Response<CallBackResult> response) {
                    //getting venues from callback and populating recyclerView

                    venues = (ArrayList<Item>) response.body().getResponse().getGroups().get(0).getItems();
                    populateVenuesRecyclerView();
                    onSwipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<CallBackResult> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.failed_venue_callback_toast, Toast.LENGTH_SHORT).show();
                }

            });
        }



    //getting current location form MainActivity through getter method

    public void getLocationFromMain() {
        MainActivity callingActivity = (MainActivity) MainActivity.activity;
        location = callingActivity.getLocation();
    }


    public void getLocation(Location location) {
        this.currentLocation = location.getLatitude() + "," + location.getLongitude();

    }


    //getting city and section from pager adapter to use in retrofit calls
    //parameters are used in retrofit call to get venue results

    public void setCity(String city, String section) {
        this.city = city;
        this.section = section;
        if (section != null) {
            getVenuesList();
        }
    }


    //getting city and section from SortingActivity to us in retrofit calls
    public void setValues(String city, String section) {
        this.city = city;
        this.section = section;
    }


    //method sets call method for foursquareService depending if city is null or not
    //runs inside getVenuesList()

    public void selectCallMethod() {
        if (city == null) {
            call = fourSquareServiceInterface.getVenuesNearby(currentLocation, section);
        } else {
            call = fourSquareServiceInterface.getVenuesByCity(city, section);
        }
    }


    //populates recyclerView with results from retrofit call, displays Toast if no results

    public void populateVenuesRecyclerView() {
        rvAdapter = new CustomRecyclerViewVenuesAdapter(venues, (ItemClickInterface) getActivity());
        recyclerView.setAdapter(rvAdapter);

        //if there are now results display toast
        if (venues.size() == 0) {
            Toast.makeText(getActivity(), R.string.venues_no_results_toast, Toast.LENGTH_SHORT).show();
        }
    }
    public void setOnSwipeRefresh() {
        onSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLocation(location);
                getVenuesList();
            }
        });
    }


}

