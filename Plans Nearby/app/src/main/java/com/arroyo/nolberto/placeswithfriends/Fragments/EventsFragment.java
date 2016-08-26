package com.arroyo.nolberto.placeswithfriends.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.arroyo.nolberto.placeswithfriends.Models.Event;
import com.arroyo.nolberto.placeswithfriends.Models.Events;
import com.arroyo.nolberto.placeswithfriends.R;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class EventsFragment extends Fragment implements LocationListener {
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
        root = inflater.inflate(R.layout.fragment_events, container, false);
        setRecyclerView(root);
        setLocationManager();
        getEventsList();
        return root;
    }

    public void getEventsList() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            eventsServiceInterface = retrofit.create(EventsServiceInterface.class);

            eventsServiceInterface.getEventsResults(resultQuery, lat, lon).enqueue(new Callback<Events>() {
                @Override
                public void onResponse(Call<Events> call, Response<Events> response) {
                    //getting article from api and inserting to database favorites table

                    eventArrayList = (ArrayList<Event>) response.body().getEvents();
                    //Log.i("check list"," "+eventArrayList.size());
                    rvAdapter = new CustomRecyclerViewEventsAdapter(eventArrayList, (ItemClickInterface) getActivity());
                    recyclerView.setAdapter(rvAdapter);

                }

                @Override
                public void onFailure(Call<Events> call, Throwable t) {
                    Toast.makeText(getActivity(), "Article API call failed", Toast.LENGTH_SHORT).show();
                    Log.i("Failed", "fail");
                }
            });

        } else {
            // the connection is not available
            Toast.makeText(getActivity(), "connection not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void setRecyclerView(View v) {
        this.root = v;
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
            Toast.makeText(getActivity(), "has location", Toast.LENGTH_SHORT).show();
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(getActivity(), "Location not available", Toast.LENGTH_SHORT).show();
        }

    }

    public void setCity(String city) {
        this.city = city;
    }
    public void setResultQuery(String resultQuery) {
        this.resultQuery = resultQuery;
        Log.d("fragment query", "result:" + resultQuery);

    }
}


