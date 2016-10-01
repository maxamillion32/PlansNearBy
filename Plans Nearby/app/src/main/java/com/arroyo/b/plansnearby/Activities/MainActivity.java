package com.arroyo.b.plansnearby.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.arroyo.b.plansnearby.Constants;
import com.arroyo.b.plansnearby.DataBaseHelper;
import com.arroyo.b.plansnearby.Fragments.EnterCityDialogFragment;
import com.arroyo.b.plansnearby.Fragments.FavsFragment;
import com.arroyo.b.plansnearby.Interfaces.ItemClickInterface;
import com.arroyo.b.plansnearby.Adapters.PagerAdapter;
import com.arroyo.b.plansnearby.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

/**
 * Activity starts application, gets values from location manager,
 * cityDialogFragment and user selection in nav drawer and sends
 * values to viewpager adapter
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, ItemClickInterface, LocationListener {
    public static Activity activity;
    private Toolbar toolbar;
    private Menu menu;
    private MenuItem item;
    private PagerAdapter adapter;
    private ViewPager viewPager;
    private LocationManager locationManager;
    private CallbackManager callbackManager;
    private Location location;
    private EnterCityDialogFragment cityDialogFragment;
    private String city, query, provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        activity = this;
        setLocationManager();
        setFacebook();
        setToolbar();
        setDrawer();
        setPageView();
        handleIntent(getIntent());

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu;
        // Inflate the menu and set search widget
        getMenuInflater().inflate(R.menu.main, menu);
        setSearchWidget();

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.location:
                cityOrNearDialog();
                enterAnotherCity();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //checking if someone is logged in to facebook, if so, logging out on click and setting new text

    public void facebookLoginLogout() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            LoginManager.getInstance().logOut();
            item.setTitle(R.string.com_facebook_loginview_log_in_button_long);
        } else {
            item.setTitle(R.string.com_facebook_loginview_log_out_action);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }

    //Method sets up facebook login call
    public void setFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setSearchWidget() {
        // set searchManager and searchableInfo
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        // link searchable info with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchableInfo);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    //running handleIntent which calls checks if there was a search, displays toast with search query
    //calling setQuery(), to send query to viewpager, tab is switched to search fragment tab when a search is made
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(MainActivity.this, getString(R.string.search_text) + query, Toast.LENGTH_SHORT).show();
            adapter.setQuery(query, city);
            viewPager.setCurrentItem(1);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //sends selection values to be passed to SortingActivity
        //to start fragments
        this.item = item;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_interests:
                //open interest activity
                Intent intent = new Intent(MainActivity.this, PickInterestsActivity.class);
                startActivity(intent);

                break;
            case R.id.nav_saved_places:
                viewSavedPlacesDialog();
                break;
            case R.id.nav_facebook_login:
                facebookLoginLogout();
                break;
            case R.id.nav_trending:
                sendCategoryValues(Constants.MAIN_CATEGORY_TRENDING);
                break;
            case R.id.nav_coffee:
                sendCategoryValues(Constants.MAIN_CATEGORY_COFFEE);
                break;
            case R.id.nav_weekend:
                sendCategoryValues(Constants.MAIN_CATEGORY_WEEKEND);
                break;
            case R.id.nav_about:
                sendCategoryValues(Constants.MAIN_CATEGORY_ABOUT);
                break;


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setPageView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_one_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_events_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_food_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_drinks_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_top_picks_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_sights_title));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    //sending nav drawer selected values to SortingActivity, to launch fragment

    public void sendCategoryValues(String category) {
        Intent intent = new Intent(this, SortingActivity.class);
        intent.putExtra(Constants.MAIN_DRAWER_CITY_KEY, city);
        intent.putExtra(Constants.MAIN_DRAWER_CATEGORY_KEY, category);
        startActivity(intent);
    }


    //method returns selectedValue from cityDialogFragment
    //calls adapter.setQuery() to pass values to pagerAdapter

    public void onUserSelectValue(String selectedValue) {
        // TODO add your implementation.
        menu.findItem(R.id.location).setIcon(R.drawable.ic_location_city_white_24dp);
        this.city = selectedValue;
        this.query = null;
        adapter.setQuery(query, city);

    }


    //method launches dialog fragment which allows user to input city when menu item is clicked
    //allows for toggling between city results and current location, changes icon to specify
    //current results

    public void cityOrNearDialog() {
        cityDialogFragment = new EnterCityDialogFragment();
        if (city == null) {
            cityDialogFragment.show(getSupportFragmentManager(), Constants.CITY_FRAGMENT);
        } else {
            menu.findItem(R.id.location).setIcon(R.drawable.ic_near_me_white_24dp);
            this.city = null;
            adapter.setQuery(query, city);
        }
    }

    //when user is viewing events by city, user has the option to long press city icon in toolbar
    //and enter another city
    public void enterAnotherCity() {
        final View v = findViewById(R.id.location);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cityDialogFragment.show(getSupportFragmentManager(), Constants.CITY_FRAGMENT);
                return false;
            }
        });
    }

    //checks database to see if there are saved venues,
    //if so, savedDialogFragment is launched displaying saved venues
    //else, toast is displayed

    public void viewSavedPlacesDialog() {
        FavsFragment savedDialogFragment = new FavsFragment();
        SQLiteDatabase db = DataBaseHelper.getInstance(this).getReadableDatabase();
        long numberOfRows = DatabaseUtils.queryNumEntries(db, DataBaseHelper.DataEntryFavorites.TABLE_NAME);
        if (numberOfRows >= 1) {
            savedDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
            savedDialogFragment.show(getSupportFragmentManager(), Constants.SAVED_VENUES_FRAGMENT);
        } else {
            Toast.makeText(this, R.string.main_no_saved_venues_toast, Toast.LENGTH_SHORT).show();
        }
    }


    //checking for Location permissions, if granted, setup locationManager and get location

    public void setLocationManager() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, R.string.location_permission_rationale_toast, Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        SplashActivity.PERMISSION_LOCATION_REQUEST_CODE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        SplashActivity.PERMISSION_LOCATION_REQUEST_CODE);

            }


        } else {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the location provider
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            locationManager.requestSingleUpdate(provider, MainActivity.this, null);
            location = locationManager.getLastKnownLocation(provider);

            // check if location is available
            if (location == null) {
                //let user know that location is unavailable, show city dialog to display venues by city
                Toast.makeText(this, R.string.location_unavailable, Toast.LENGTH_SHORT).show();
                View parentView = findViewById(R.id.content_view);
                Snackbar.make(parentView, R.string.snackbar_text, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.snack_bar_action_text_city, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cityOrNearDialog();
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == SplashActivity.PERMISSION_LOCATION_REQUEST_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocationManager();
                //Displaying a toast
                Toast.makeText(this, R.string.permission_granted_toast, Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, R.string.permission_denied_tost, Toast.LENGTH_LONG).show();
            }
        }
    }

    //if location changes it is passed to fragments through viewpager, tabs are updated
    //if user is viewing events with location off and then turns location on, we set
    //new location equal to this.location, and pass values to fragments using viewpager adapter
    //this updates views as soon as location is available
    @Override
    public void onLocationChanged(Location location) {
        if (this.location == null) {
            this.location = location;
            if (adapter != null) {
                adapter.setQuery(query, city);
            }
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLocation() {
        return location;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, R.string.connection_unavailable, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onItemClicked(String selectedItem) {

    }

    //facebook login result being passed to callback manager
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}

