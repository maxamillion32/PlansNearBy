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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Adapters.CustomRecyclerViewAdapter;
import com.arroyo.nolberto.placeswithfriends.Interfaces.FourSquareServiceInterface;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.CallBackResult;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.Item;
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
public class FoodFragment extends Fragment implements LocationListener {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static String baseURL = "https://api.foursquare.com/v2/";
    ArrayList<Item> places;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private LocationManager locationManager;
    private String provider;
    private String currentLocation;
    private String city;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    FourSquareServiceInterface fourSquareServiceInterface;
    ItemClickInterface onItemClickedListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemClickedListener = (ItemClickInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement interface  ");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(com.arroyo.nolberto.placeswithfriends.R.layout.fragment_food, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rvLayoutManager);
        connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        setLocationManager();
        getVenuesList();
        return root;
    }

    public void getVenuesList() {
        if (city == null) {
            if (networkInfo != null && networkInfo.isConnected()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                fourSquareServiceInterface = retrofit.create(FourSquareServiceInterface.class);

                fourSquareServiceInterface.getFoodNearby("40.7,-74").enqueue(new Callback<CallBackResult>() {
                    @Override
                    public void onResponse(Call<CallBackResult> call, Response<CallBackResult> response) {
                        //getting article from api and inserting to database favorites table

                        places = (ArrayList<Item>) response.body().getResponse().getGroups().get(0).getItems();
                        rvAdapter = new CustomRecyclerViewAdapter(places, (ItemClickInterface) getActivity());
                        recyclerView.setAdapter(rvAdapter);


                    }

                    @Override
                    public void onFailure(Call<CallBackResult> call, Throwable t) {

                    }


                });
            }
        } else {
            if (networkInfo != null && networkInfo.isConnected()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                fourSquareServiceInterface = retrofit.create(FourSquareServiceInterface.class);

                fourSquareServiceInterface.getFoodVenues(city).enqueue(new Callback<CallBackResult>() {
                    @Override
                    public void onResponse(Call<CallBackResult> call, Response<CallBackResult> response) {
                        //getting article from api and inserting to database favorites table

                        places = (ArrayList<Item>) response.body().getResponse().getGroups().get(0).getItems();
                        rvAdapter = new CustomRecyclerViewAdapter(places, (ItemClickInterface) getActivity());
                        recyclerView.setAdapter(rvAdapter);


                    }

                    @Override
                    public void onFailure(Call<CallBackResult> call, Throwable t) {

                    }


                });
            }

        }
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

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location.getLatitude() + "," + location.getLongitude();

    }

    public void setCity(String city) {
        this.city = city;
        getVenuesList();
    }
}

