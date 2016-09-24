package com.arroyo.nolberto.placeswithfriends.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Constants;
import com.arroyo.nolberto.placeswithfriends.DataBaseHelper;
import com.arroyo.nolberto.placeswithfriends.Interfaces.FourSquareServiceInterface;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.CallBackResult;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.Venue;
import com.arroyo.nolberto.placeswithfriends.R;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *activity displays venue details by getting id from custom adapter and making a call using
 * retrofit
 *
 **/

public class VenueDetailsActivity extends AppCompatActivity implements ItemClickInterface, View.OnClickListener {
    private ImageView venueImage,share, directions,saveVenue,venueUrl;
    private TextView venueTitle, venueAddress, venueCategory, venueDescription,tipsReviews;
    private ListView listView;
    private DataBaseHelper helper;
    private FourSquareServiceInterface serviceInterface;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ArrayList<String> tipsItems;
    private ArrayAdapter<String> adapter;
    private Venue venue;
    private String venueId;


    @Override
    public void onItemClicked(String selectedItem) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);
        setViews();
        receiveVenueSelectedId();
        getVenueFromId();
        share.setOnClickListener(this);
        directions.setOnClickListener(this);
        saveVenue.setOnClickListener(this);
        venueUrl.setOnClickListener(this);

    }

    public void setViews() {
        venueImage = (ImageView) findViewById(R.id.activity_details_venue_image);
        venueTitle = (TextView) findViewById(R.id.activity_details_venue_title);
        venueCategory = (TextView) findViewById(R.id.activity_details_venue_category);
        venueAddress = (TextView) findViewById(R.id.activity_details_venue_address);
        venueDescription = (TextView) findViewById(R.id.activity_details_venue_description);
        share = (ImageView) findViewById(R.id.activity_details_venue_share_bttn);
        directions = (ImageView) findViewById(R.id.activity_details_venue_directions_bttn);
        tipsReviews = (TextView) findViewById(R.id.activity_venue_details_tips);
        saveVenue = (ImageView)findViewById(R.id.activity_details_venue_interested_bttn);
        venueUrl = (ImageView)findViewById(R.id.activity_details_venue_url);
    }


    //get event making a call using retrofit
    //populate views with venue properties
    public void getVenueFromId() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FOURSQUARE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(FourSquareServiceInterface.class);

        serviceInterface.getVenueById(venueId).enqueue(new Callback<CallBackResult>() {

            @Override
            public void onResponse(Call<CallBackResult> call, Response<CallBackResult> response) {
                //getting article from api and inserting to database favorites table
                venue = response.body().getResponse().getVenue();
                venueTitle.setText(venue.getName());
                venueAddress.setText(venue.getLocation().getAddress());
                venueCategory.setText(venue.getCategories().get(0).getName());
                tipsReviews.setText(R.string.venues_details_tips);
                if (venue.getHours()!=null){
                    venueDescription.setText(venue.getHours().getStatus());
                }

                setVenueImage();
                setTipsReviewsListView();



            }


            @Override
            public void onFailure(Call<CallBackResult> call, Throwable t) {
                Toast.makeText(VenueDetailsActivity.this, R.string.venue_details_failed_api_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // get intent from customRecyclerViewAdapter
    public void receiveVenueSelectedId() {
        Intent intent = getIntent();
        this.venueId = intent.getStringExtra(Constants.SELECTED_VENUE_ID_KEY);
    }


    //method prepares shareDialog to share to facebook
    public void shareToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(venue.getName())
                    .setContentDescription(venue.getContact().getFormattedPhone())
                    .setContentUrl(Uri.parse(venue.getUrl()))
                    .build();

            shareDialog.show(linkContent);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_details_venue_share_bttn:
                //shares current event to facebook, have option to choose friends
                  shareToFacebook();
                break;

            case R.id.activity_details_venue_directions_bttn:
                // opens maps in directions view
                String map = "http://maps.google.com/maps?daddr=" + venue.getLocation().getAddress();
                Intent openMapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(openMapsIntent);

                break;

            case R.id.activity_details_venue_interested_bttn:
                //checks if venue is already in database, if so deletes item  and displays toast
                //giving user notice of added venue or deleted venue
                helper= DataBaseHelper.getInstance(VenueDetailsActivity.this);
                if (helper.exists(venueId)) {
                    helper.deleteFavoritesItem(venueId);
                    Toast.makeText(VenueDetailsActivity.this, R.string.venue_details_removed_venue_toast, Toast.LENGTH_SHORT).show();
                } else {
                    helper = DataBaseHelper.getInstance(VenueDetailsActivity.this);
                    helper.insertRowFavorities(venue);
                    Toast.makeText(VenueDetailsActivity.this, R.string.venue_details_added_venue_favs, Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.activity_details_venue_url:
                //opens foursquare info page for venue
                Uri uri = Uri.parse(venue.getTips().getGroups().get(0).getItems().get(0).getCanonicalUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


                break;

        }
    }
    public void setVenueImage(){
        String suffix =venue.getPhotos().getGroups().get(0).getItems().get(0).getSuffix();
        String prefix = venue.getPhotos().getGroups().get(0).getItems().get(0).getPrefix();
        String imageUrl = prefix+Constants.VENUE_IMAGE_SIZE+suffix;
        Picasso.with(getApplicationContext()).load(imageUrl).into(venueImage);
    }
    public void setTipsReviewsListView(){
        listView = (ListView)findViewById(R.id.tips_list_view);
        tipsItems= new ArrayList<String>();
        for (int i=0; i<venue.getTips().getGroups().get(0).getItems().size();i++){
            tipsItems.add(venue.getTips().getGroups().get(0).getItems().get(i).getText());
        }
        adapter = new ArrayAdapter<String>(VenueDetailsActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,tipsItems);
        listView.setDividerHeight(12);
        listView.setAdapter(adapter);
    }
}


