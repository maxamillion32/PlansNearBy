package com.arroyo.b.plansnearby.Interfaces;

import com.arroyo.b.plansnearby.BuildConfig;
import com.arroyo.b.plansnearby.Models.FourSquareModels.CallBackResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */
public interface FourSquareServiceInterface {

    //get venue by id
    @GET("venues/{venue_id}?&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID + "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET + "&v=20160913")
    Call<CallBackResult> getVenueById(@Path("venue_id") String venueId);

    //get venues by city, returns only venues that are open now and sorts by default rating
    @GET("venues/explore/?&venuePhotos=1&openNow=1&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID + "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET + "&v=20160913")
    Call<CallBackResult> getVenuesByCity(@Query("near") String near, @Query("section") String section);

    //get venues by current location, sort by distance,takes in section and current location
    @GET("venues/explore/?&venuePhotos=1&openNow=1&sortByDistance=1&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID + "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET + "&v=20160913")
    Call<CallBackResult> getVenuesNearby(@Query("ll") String latLon, @Query("section") String section);


}
