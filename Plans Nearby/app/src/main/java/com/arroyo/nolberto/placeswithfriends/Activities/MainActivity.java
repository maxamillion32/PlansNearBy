package com.arroyo.nolberto.placeswithfriends.Activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Fragments.EnterCityDialogFragment;
import com.arroyo.nolberto.placeswithfriends.Fragments.ForYouEventsFragment;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Adapters.PagerAdapter;
import com.arroyo.nolberto.placeswithfriends.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener ,ItemClickInterface {
    private static final int FB_SIGN_IN= 1;
    private PagerAdapter adapter;
    private ViewPager viewPager;
    private CallbackManager callbackManager;
    private Toolbar toolbar;
    private EnterCityDialogFragment cityDialogFragment;
    private String city;
    private String query;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        loginFirebase();
        setFacebook();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDrawer();
        setPageView();
        handleIntent(getIntent());
        cityDialogFragment= new EnterCityDialogFragment();

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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // set searchManager and searchableInfo
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        // link searchable info with the SearchView
        SearchView searchView =(SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchableInfo);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;

            case R.id.location:
                if (city== null) {
                    cityDialogFragment.show(getSupportFragmentManager(), "city fragment");
                }else {
                    menu.findItem(R.id.location).setIcon(R.drawable.ic_my_location_white_24dp);
                    this.city = null;
                    adapter.setQuery(query, city);
                }
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            //TODO:

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            //checking if someone is logged in to facebook, if so, logging out on click and setting new text
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                LoginManager.getInstance().logOut();
                AuthUI.getInstance().signOut(this);
                item.setTitle(R.string.com_facebook_loginview_log_in_button_long);
            } else {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                item.setTitle(R.string.com_facebook_loginview_log_out_action);
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.FACEBOOK_PROVIDER)
                        .build(),1);

            }


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setPageView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_one));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_events_name));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_near_me_name));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
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
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, R.string.connection_unavailable, Toast.LENGTH_SHORT).show();

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
            Log.d("mainActivity query","result:"+query +city);
        }

    }

    @Override
    public void onItemClicked(String selectedItem) {

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
    public void setDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onUserSelectValue(String selectedValue) {
        // TODO add your implementation.
        Log.i("selectedValue", selectedValue);
        menu.findItem(R.id.location).setIcon(R.drawable.ic_location_city_white_24dp);
        this.city = selectedValue;
        this.query=null;
        adapter.setQuery(query, city);

    }
    public void loginFirebase(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
       if (auth.getCurrentUser()!= null) {
       Toast.makeText(MainActivity.this,auth.getCurrentUser().getDisplayName()+ "signed on",Toast.LENGTH_SHORT).show();
       }else {
        startActivityForResult(AuthUI.getInstance()
                   .createSignInIntentBuilder()
                   .setProviders(
                           AuthUI.FACEBOOK_PROVIDER)
                   .build(),1);
           LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
       }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FB_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
                Toast.makeText(MainActivity.this, "sign in ", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
