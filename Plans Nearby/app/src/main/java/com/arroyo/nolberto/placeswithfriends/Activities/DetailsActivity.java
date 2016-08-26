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
import com.arroyo.nolberto.placeswithfriends.Interfaces.ItemClickInterface;
import com.arroyo.nolberto.placeswithfriends.Models.Event;
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

public class DetailsActivity extends AppCompatActivity implements ItemClickInterface, View.OnClickListener {
    private static String baseURL = "https://www.eventbriteapi.com/";
    private ImageView eventImage;
    private TextView eventTitle, eventAddress, eventCategory, eventDescription;
    private Button share, interested, directions, tickets;
    private Event event;
    private String eventId;
    private EventsServiceInterface eventsServiceInterface;
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
        recieveEventSelectedId();
        getEventFromId();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventToCalendar();
                Snackbar.make(view, "Add Event to calendar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        tickets.setOnClickListener(this);
        share.setOnClickListener(this);
        directions.setOnClickListener(this);
        interested.setOnClickListener(this);
    }

    public void setViews() {
        eventImage = (ImageView) findViewById(R.id.activity_details_image);
        eventTitle = (TextView) findViewById(R.id.activity_details_title);
        eventCategory = (TextView) findViewById(R.id.activity_details_category);
        eventAddress = (TextView) findViewById(R.id.activity_details_address);
        eventDescription = (TextView) findViewById(R.id.activity_details_description);
        share = (Button) findViewById(R.id.activity_details_share_bttn);
        tickets = (Button) findViewById(R.id.activity_details_tickets_bttn);
        directions = (Button) findViewById(R.id.activity_details_directions_bttn);
        interested = (Button) findViewById(R.id.activity_details_interested_bttn);
    }

    public void getEventFromId() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            eventsServiceInterface = retrofit.create(EventsServiceInterface.class);

            eventsServiceInterface.getEventById(eventId).enqueue(new Callback<Event>() {

                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {
                    //getting article from api and inserting to database favorites table
                    event = response.body();

                    eventTitle.setText(event.getName().getText());
                    eventDescription.setText(Html.fromHtml(event.getDescription().getHtml()));
                    eventDescription.setMovementMethod(LinkMovementMethod.getInstance());

                    if (event.getVenue() != null) {
                        eventAddress.setText(event.getVenue().getAddress().getLocalizedAddressDisplay());
                    }

                    if (event.getCategory() != null) {
                        eventCategory.setText(event.getCategory().getNameLocalized());
                    }

                    if (event.getLogo() != null) {
                        Picasso.with(DetailsActivity.this).load(event.getLogo().getUrl()).into(eventImage);
                    } else {
                        eventImage.setImageResource(R.drawable.no_images);
                    }

                }


                @Override
                public void onFailure(Call<Event> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, "Event API call failed", Toast.LENGTH_SHORT).show();
                    Log.i("Failed", "fail");
                }
            });
            // the connection is available
        } else {
            // the connection is not available
        }
    }

    // this method
    public void recieveEventSelectedId() {
        Intent intent = getIntent();
        eventId = intent.getStringExtra("tag");
    }

    //adds current event to calender
    public void addEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, event.getName().getText());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription().getText());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getVenue().getAddress().getLocalizedAddressDisplay());
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }

    //method prepares shareDialog to share to facebook
    public void shareToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(event.getName().getText())
                    .setContentDescription(event.getDescription().getText())
                    .setContentUrl(Uri.parse(event.getLogo().getUrl()))
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
                String map = "http://maps.google.com/maps?daddr=" + event.getVenue().getAddress().getLocalizedAddressDisplay();
                Intent openMapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(openMapsIntent);

                //Intent openMapsNavigationIntent = new Intent(android.content.Intent.ACTION_VIEW,
                //      Uri.parse("google.navigation:q=" + event.getVenue().getAddress().getLocalizedAddressDisplay()));
                //startActivity(intentMap);

                break;
            case R.id.activity_details_interested_bttn:

                break;

            case R.id.activity_details_tickets_bttn:
                // opens browser to buy tickets for event directly from eventbrite
                Uri uri = Uri.parse(event.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}

