package com.arroyo.b.plansnearby.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.arroyo.b.plansnearby.Activities.MainActivity;
import com.arroyo.b.plansnearby.Adapters.CustomRecyclerViewEventsAdapter;
import com.arroyo.b.plansnearby.Utils.Constants;
import com.arroyo.b.plansnearby.Interfaces.EventsServiceInterface;
import com.arroyo.b.plansnearby.Interfaces.ItemClickInterface;
import com.arroyo.b.plansnearby.Models.EventBriteModels.Event;
import com.arroyo.b.plansnearby.Models.EventBriteModels.Events;
import com.arroyo.b.plansnearby.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nolbertoarroyo on 9/1/16.
 * sets event tabs,processed data comming from MainActivity and calls api service
 * to populate recyclerviews
 */
public class EventsFragment extends Fragment {
    private static final int VISIBLE_THRESHOLD = 10;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private RecyclerView.OnScrollListener scrollListener;
    private View root;
    private TextView noResults;
    private SwipeRefreshLayout onSwipeRefresh;
    private ItemClickInterface onItemClickedListener;
    private EventsServiceInterface eventsServiceInterface;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private ArrayList<Event> eventArrayList;
    private Call<Events> call;
    private Location location;
    private String resultQuery, lon, lat, city, categories, weekendOnly, fragName;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = 1;

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
        noResults = (TextView) root.findViewById(R.id.events_empty_view);
        setConnectionManager();
        categories = toDelimitedString(loadArray(Constants.PREFS_INTEREST_ARRAY, getActivity()));
        getLocationFromMain();
        getLocation(location);
        checkWhichFragmentToStart();
        getEventsCategoryList();
        setOnSwipeRefresh();
        return root;
    }

    //method makes retrofit call using Eventbrite service,
    //returns results based on interests selected by user
    //populates recyclerView with results
    public void getEventsCategoryList() {

        if (networkInfo == null || !networkInfo.isConnected()) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.EVENT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventsServiceInterface = retrofit.create(EventsServiceInterface.class);
        selectCallMethod();
        onSwipeRefresh.setRefreshing(true);


        call.enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                isLoading = false;
                //if page is greater than 1, then we are taking new response and updating eventsArraylist
                //we notify adapter with new data
                //to update views, if page is 1 it means it is the first call and will populate views
                if (page > 1) {
                    ArrayList<Event> updateList = (ArrayList<Event>) response.body().getEvents();
                    updateEventsList(updateList);

                } else {
                    eventArrayList = (ArrayList<Event>) response.body().getEvents();
                    populateEventsRecyclerView();
                    onSwipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.api_fail_toast, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void setRecyclerView(View v) {
        this.root = v;
        onSwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipe_efresh_layout);
        recyclerView = (RecyclerView) v.findViewById(R.id.events_frag_recycler_view);
        rvLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rvLayoutManager);
        scrollListener = new RecyclerView.OnScrollListener() {
            //onScrolled runs loadMore() when it reaches the bottom of the list

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = rvLayoutManager.getChildCount();
                int totalItemCount = rvLayoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)
                        recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - VISIBLE_THRESHOLD)
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= Constants.PAGE_SIZE) {
                        loadMore();

                    }
                }

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    //getting location object from main activity
    public void getLocationFromMain() {
        MainActivity callingActivity = (MainActivity) MainActivity.activity;
        location = callingActivity.getLocation();
    }

    //takes location and extracts latitude and longitude to use in api call
    public void getLocation(Location location) {
        if (location != null) {

            lat = String.valueOf((location.getLatitude()));
            lon = String.valueOf((location.getLongitude()));
        }
    }


    public void setOnSwipeRefresh() {
        onSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resultQuery = null;
                getLocation(location);
                getEventsCategoryList();
            }
        });
    }

    public void setCategoryQuery(String resultQuery, String city, String fragmentName) {
        this.page = 1;
        this.resultQuery = resultQuery;
        this.city = city;
        this.fragName = fragmentName;
        //check if location is valid
        getLocationFromMain();
        if (location != null) {
            getLocation(location);
        }
        //if all events fragment set categories to null, so all events are called
        if (fragName.equalsIgnoreCase(Constants.TAB_ALL_EVENTS)) {
            categories = null;
        }
        getEventsCategoryList();
    }

    //method takes in array of strings and adds a comma in between strings
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

    //method loads interest array from shared prefs
    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHAREPREFS, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public void setConnectionManager() {
        connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
    }

    // method checks if city is null or not, and sets retrofit call to get m
    public void selectCallMethod() {
        if (city != null) {
            lat = null;
            lon = null;
        }
        checkWhichFragmentToStart();
        String sPage = String.valueOf(page);
        call = eventsServiceInterface.getEventsCatResults(resultQuery, city, lat, lon, weekendOnly, categories, sPage);
    }

    /*
     * populates recyclerView with results from retrofit call, displays Toast if no results
     * sets recyclerView to GONE and makes no results textView visible
     */
    public void populateEventsRecyclerView() {
        rvAdapter = new CustomRecyclerViewEventsAdapter(eventArrayList, (ItemClickInterface) getActivity());
        recyclerView.setAdapter(rvAdapter);

        //if there are now results display toast
        if (eventArrayList.size() == 0) {

            Toast.makeText(getActivity(), R.string.no_events_available, Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
        } else {

            recyclerView.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
        }

    }

    //gets values from SortingActivity and sets values
    public void setValues(String city, String weekend) {
        this.city = city;
        this.weekendOnly = weekend;
    }

    //this method runs onCreateView, checks which fragment to start
    public void checkWhichFragmentToStart() {
        if (weekendOnly != null ||
                fragName.equalsIgnoreCase(Constants.TAB_ALL_EVENTS)) {
            categories = null;
        }
    }

    //changes increments page and makes a new call to update data
    public void loadMore() {
        isLoading = true;
        this.page++;
        getEventsCategoryList();

    }

    //takes new list returned from api call, and added to existing list if it has data
    public void updateEventsList(ArrayList<Event> updateList) {
        if (!updateList.isEmpty()) {
            for (Event event : updateList) {
                eventArrayList.add(event);
                rvAdapter.notifyDataSetChanged();
                onSwipeRefresh.setRefreshing(false);
            }
        } else {
            onSwipeRefresh.setRefreshing(false);
            isLastPage = true;
        }
    }


}
