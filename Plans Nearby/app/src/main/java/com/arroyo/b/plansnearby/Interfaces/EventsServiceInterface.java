package com.arroyo.b.plansnearby.Interfaces;

import com.arroyo.b.plansnearby.BuildConfig;
import com.arroyo.b.plansnearby.Models.EventBriteModels.Event;
import com.arroyo.b.plansnearby.Models.EventBriteModels.Events;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nolbertoarroyo on 8/18/16.
 */
public interface EventsServiceInterface {


    //get all events
    @GET("events/search/?token=" + BuildConfig.EVENTBRITE_TOKEN + "")
    Call<Events> getEvents();

    //get events by id
    @GET("/v3/events/{id}/?expand=venue,category,ticket_classes&token=" + BuildConfig.EVENTBRITE_TOKEN + "")
    Call<Event> getEventById(@Path("id") String id);

    //get events by current location, sorted by date, takes in query, city, longitude, latitude, categories
    //strings can be null
    @GET("events/search/?expand=venue,category,ticket_classes,display_settings&sort_by=date&token=" + BuildConfig.EVENTBRITE_TOKEN + "")
    Call<Events> getEventsCatResults(
            @Query("q") String query,
            @Query("venue.city") String city, @Query("location.latitude") String lat,
            @Query("location.longitude") String lon, @Query("start_date.keyword") String weekendOnly,
            @Query("categories") String category);


}
