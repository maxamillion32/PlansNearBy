package com.arroyo.nolberto.placeswithfriends.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Adapters.CustomRecyclerViewEventsAdapter;
import com.arroyo.nolberto.placeswithfriends.Interfaces.EventsServiceInterface;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Models.EventBriteModels.Event;
import com.arroyo.nolberto.placeswithfriends.Models.EventBriteModels.Events;
import com.arroyo.nolberto.placeswithfriends.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nolbertoarroyo on 9/1/16.
 */
public class ForYouEventsFragment extends Fragment implements LocationListener {


    private static String baseURL = "https://www.eventbriteapi.com/v3/";
    private ArrayList<Event> eventArrayList;
    private String resultQuery;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private ItemClickInterface onItemClickedListener;
    private EventsServiceInterface eventsServiceInterface;
    private View root;
    private LocationManager locationManager;
    private String provider;
    private String lon;
    private String lat;
    private String city;
    private SwipeRefreshLayout onSwipeRefresh;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    int category;
    String categories;


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
        root = inflater.inflate(R.layout.fragment_for_you_events, container, false);
        setRecyclerView(root);
        connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        setLocationManager();
        getEventsCategoryList();
        setOnSwipeRefresh();
        return root;
    }


    public void getEventsCategoryList() {


        if (city == null) {


            if (networkInfo != null && networkInfo.isConnected()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                eventsServiceInterface = retrofit.create(EventsServiceInterface.class);
                onSwipeRefresh.setRefreshing(true);


                eventsServiceInterface.getEventsCatResults(resultQuery, lat, lon, toDelimitedString(loadArray("interests", getActivity()))).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {


                        //getting article from api and inserting to database favorites table
                        eventArrayList = (ArrayList<Event>) response.body().getEvents();
                        //Log.i("check list"," "+eventArrayList.size());
                        rvAdapter = new CustomRecyclerViewEventsAdapter(eventArrayList, (ItemClickInterface) getActivity());
                        recyclerView.setAdapter(rvAdapter);
                        onSwipeRefresh.setRefreshing(false);


                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.api_fail_toast, Toast.LENGTH_SHORT).show();
                        Log.i("Failed", "fail");
                    }
                });

            } else {
                // the connection is not available
                //            Toast.makeText(getActivity(),R.string.connection_unavailable, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (networkInfo != null && networkInfo.isConnected()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                eventsServiceInterface = retrofit.create(EventsServiceInterface.class);
                onSwipeRefresh.setRefreshing(true);

                eventsServiceInterface.getEventsCatResults(resultQuery, city, toDelimitedString(loadArray("interests", getActivity()))).enqueue(new Callback<Events>() {
                    @Override
                    public void onResponse(Call<Events> call, Response<Events> response) {
                        //getting event from api
                        eventArrayList = (ArrayList<Event>) response.body().getEvents();
                        rvAdapter = new CustomRecyclerViewEventsAdapter(eventArrayList, (ItemClickInterface) getActivity());
                        recyclerView.setAdapter(rvAdapter);
                        onSwipeRefresh.setRefreshing(false);


                    }

                    @Override
                    public void onFailure(Call<Events> call, Throwable t) {
                        Toast.makeText(getActivity().getParent(), R.string.api_fail_toast, Toast.LENGTH_SHORT).show();
                        Log.i("Failed", "fail");
                    }
                });

            } else {
                // the connection is not available
                //      Toast.makeText(getActivity().getParent(), R.string.connection_unavailable, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setRecyclerView(View v) {
        this.root = v;
        onSwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.events_frag_recycler_view);
        rvLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rvLayoutManager);

    }


    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf((location.getLatitude()));
        lon = String.valueOf((location.getLongitude()));

    }

    public void setLocationManager() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(getActivity(), R.string.location_unavailable, Toast.LENGTH_SHORT).show();
        }

    }

    public void setOnSwipeRefresh() {
        onSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resultQuery = null;
                setLocationManager();
                getEventsCategoryList();
            }
        });
    }

    public void setCategoryQuery(String resultQuery, String city) {
        this.resultQuery = resultQuery;
        this.city = city;
        getEventsCategoryList();
        Log.d("fragment query", "result:" + resultQuery + city + category);

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public static String toDelimitedString(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }
    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }


}
