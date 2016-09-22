package com.arroyo.nolberto.placeswithfriends.Interfaces;

import com.arroyo.nolberto.placeswithfriends.BuildConfig;
import com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels.CallBackResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */
public interface FourSquareServiceInterface {


    @GET("venues/{venue_id}?&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID + "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET + "&v=20160913")
    Call<CallBackResult> getVenueById(@Path("venue_id") String venueId);

    @GET("venues/explore/?&venuePhotos=1&openNow=1&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID + "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET + "&v=20160913")
    Call<CallBackResult> getVenuesByCity(@Query("near") String near, @Query("section") String section);

    @GET("venues/explore/?&venuePhotos=1&openNow=1&sortByDistance=1&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID + "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET + "&v=20160913")
    Call<CallBackResult> getVenuesNearby(@Query("ll") String latLon, @Query("section") String section);


}
