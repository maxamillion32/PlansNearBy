package com.arroyo.nolberto.placeswithfriends.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Interfaces.EventsServiceInterface;
import com.arroyo.nolberto.placeswithfriends.Interfaces.FourSquareServiceInterface;
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Models.EventBriteModels.Event;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.CallBackResult;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.Venue;
import com.arroyo.nolberto.placeswithfriends.R;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class VenueDetailsActivity extends AppCompatActivity implements ItemClickInterface, View.OnClickListener {
    private static String baseURL = "https://api.foursquare.com/v2/";
    private ImageView venueImage,share, directions;
    private TextView venueTitle, venueAddress, venueCategory, venueDescription;
    private Venue venue;
    private String venueId;
    private FourSquareServiceInterface serviceInterface;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;


    @Override
    public void onItemClicked(String selectedItem) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setViews();
        recieveVenueSelectedId();
        getVenueFromId();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Event to calendar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        share.setOnClickListener(this);
        directions.setOnClickListener(this);
    }

    public void setViews() {
        venueImage = (ImageView) findViewById(R.id.activity_details_image);
        venueTitle = (TextView) findViewById(R.id.activity_details_title);
        venueCategory = (TextView) findViewById(R.id.activity_details_category);
        venueAddress = (TextView) findViewById(R.id.activity_details_address);
        venueDescription = (TextView) findViewById(R.id.activity_details_description);
        share = (ImageView) findViewById(R.id.activity_details_share_bttn);
        directions = (ImageView) findViewById(R.id.activity_details_directions_bttn);
    }

    public void getVenueFromId() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(FourSquareServiceInterface.class);

        serviceInterface.getVenueById(venueId).enqueue(new Callback<CallBackResult>() {

            @Override
            public void onResponse(Call<CallBackResult> call, Response<CallBackResult> response) {
                //getting article from api and inserting to database favorites table
                venue = response.body().getResponse().getVenue();
                Log.i("venue",venue.getId());

                venueTitle.setText(venue.getName());
                venueAddress.setText(venue.getLocation().getAddress());
                String suffix =venue.getPhotos().getGroups().get(0).getItems().get(0).getSuffix();
                String prefix = venue.getPhotos().getGroups().get(0).getItems().get(0).getPrefix();

                String imageUrl = prefix+"original"+suffix;
                Picasso.with(getApplicationContext()).load(imageUrl).into(venueImage);
                //venueDescription.setText(Html.fromHtml(venue.getDescription().getHtml()));
                //venueDescription.setMovementMethod(LinkMovementMethod.getInstance());



            }


            @Override
            public void onFailure(Call<CallBackResult> call, Throwable t) {
                Toast.makeText(VenueDetailsActivity.this, "failed", Toast.LENGTH_SHORT).show();
                Log.i("Failed", "fail");
            }
        });
    }

    // this method
    public void recieveVenueSelectedId() {
        Intent intent = getIntent();
        this.venueId = intent.getStringExtra("venueId");
        Log.i("venueid",venueId+ " ");
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

            case R.id.activity_details_share_bttn:
                //shares current event to facebook, have option to choose friends
                shareToFacebook();
                break;

            case R.id.activity_details_directions_bttn:
                // opens maps in directions view
                String map = "http://maps.google.com/maps?daddr=" + venue.getLocation().getAddress();
                Intent openMapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(openMapsIntent);

                //Intent openMapsNavigationIntent = new Intent(android.content.Intent.ACTION_VIEW,
                //      Uri.parse("google.navigation:q=" + event.getVenue().getAddress().getLocalizedAddressDisplay()));
                //startActivity(intentMap);

                break;
            case R.id.activity_details_interested_bttn:

                break;

        }
    }
}


