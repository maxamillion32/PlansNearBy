package com.arroyo.nolberto.placeswithfriends;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arroyo.nolberto.placeswithfriends.Models.Event;
import com.arroyo.nolberto.placeswithfriends.Models.Events;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity implements ItemClickInterface {
    private String eventId;
    private ImageView eventImage;
    private TextView eventTitle;
    private TextView eventAddress;
    private TextView eventCategory;
    private TextView eventDescription;
    private EventsServiceInterface eventsServiceInterface;
    private static String baseURL = "https://www.eventbriteapi.com/";
    private Event event;
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
                //addEventToCalendar();
                shareToFacebook();
                Snackbar.make(view, "Add Event to calendar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    public void setViews() {
        eventImage = (ImageView) findViewById(R.id.activity_details_image);
        eventTitle = (TextView) findViewById(R.id.activity_details_title);
        eventCategory = (TextView) findViewById(R.id.activity_details_category);
        eventAddress = (TextView) findViewById(R.id.activity_details_address);
        eventDescription = (TextView) findViewById(R.id.activity_details_description);
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
                    eventDescription.setText(event.getDescription().getText());

                    if (event.getVenue() != null) {
                        eventAddress.setText(event.getVenue().getAddress().getAddress1());
                    }

                    if (event.getCategory() != null) {
                        eventCategory.setText(event.getCategory().getNameLocalized());
                    }

                    if (event.getLogo() != null) {
                        Picasso.with(DetailsActivity.this).load(event.getLogo().getUrl()).into(eventImage);
                    }else{
                        eventImage.setImageResource(R.drawable.no_images);
                    }

                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {
                    Toast.makeText(DetailsActivity.this, "Article API call failed", Toast.LENGTH_SHORT).show();
                    Log.i("Failed", "fail");
                }
            });
            // the connection is available
        } else {
            // the connection is not available
        }
    }

    public void recieveEventSelectedId() {
        Intent intent = getIntent();
        eventId = intent.getStringExtra("tag");
        Log.i("tagging", eventId);
    }
    public void addEventToCalendar(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, event.getName().getText());
        intent.putExtra(CalendarContract.Events.DESCRIPTION,  event.getDescription().getText());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getVenue().getAddress().getAddress1());
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


    }}

