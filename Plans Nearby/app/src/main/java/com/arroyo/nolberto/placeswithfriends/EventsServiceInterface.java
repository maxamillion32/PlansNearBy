package com.arroyo.nolberto.placeswithfriends;

import com.arroyo.nolberto.placeswithfriends.Models.Event;
import com.arroyo.nolberto.placeswithfriends.Models.Events;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nolbertoarroyo on 8/18/16.
 */
public interface EventsServiceInterface {

    @GET("events/search/?token=BCE2TXE45OB66VQPNBJ3")
    Call<Events> getEvents();

    @GET ("/v3/events/{id}/?expand=venue,category,ticket_classes&token=BCE2TXE45OB66VQPNBJ3")
    Call<Event> getEventById(@Path("id") String id);

    @GET ("events/search/?expand=venue,category,ticket_classes&sort_by=date&token=BCE2TXE45OB66VQPNBJ3")
    Call<Events> getEventsResults(@Query("q")String query);

    @GET ("events/search/?expand=venue,category,ticket_classes&sort_by=date&token=BCE2TXE45OB66VQPNBJ3")
    Call<Events> getEventsResults(@Query("q")String query,@Query("location.latitude")String lat, @Query("location.longitude")String lon);

    @GET ("events/search/?expand=venue,category,ticket_classes&sort_by=date&token=BCE2TXE45OB66VQPNBJ3")
    Call<Events> getEventsResults(@Query("venue.city")String city, @Query("q")String query);





}
