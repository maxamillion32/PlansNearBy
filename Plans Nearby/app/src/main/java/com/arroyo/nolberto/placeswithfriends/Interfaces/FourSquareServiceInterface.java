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

    @GET("venues/explore/?&venuePhotos=1&section=drinks&openNow=1&client_id="+ BuildConfig.FOURSQUARE_CLIENT_ID+"&client_secret="+BuildConfig.FOURSQUARE_CLIENT_SECRET+"&v=20160913")
    Call<CallBackResult> getDrinksVenues(@Query("near") String near);

    @GET("venues/explore/?&venuePhotos=1&section=food&openNow=1&client_id="+ BuildConfig.FOURSQUARE_CLIENT_ID+"&client_secret="+BuildConfig.FOURSQUARE_CLIENT_SECRET+"&v=20160913")
    Call<CallBackResult> getFoodVenues(@Query("near") String near);

    @GET("venues/explore/?&venuePhotos=1&section=drinks&openNow=1&sortByDistance=1&client_id="+ BuildConfig.FOURSQUARE_CLIENT_ID+"&client_secret="+BuildConfig.FOURSQUARE_CLIENT_SECRET+"&v=20160913")
    Call<CallBackResult> getDrinksNearby(@Query("ll") String latLon);

    @GET("venues/explore/?&venuePhotos=1&section=food&openNow=1&sortByDistance=1&client_id="+ BuildConfig.FOURSQUARE_CLIENT_ID+"&client_secret="+BuildConfig.FOURSQUARE_CLIENT_SECRET+"&v=20160913")
    Call<CallBackResult> getFoodNearby(@Query("ll") String latLon);

    @GET("venues/{venue_id}?&client_id="+ BuildConfig.FOURSQUARE_CLIENT_ID+"&client_secret="+BuildConfig.FOURSQUARE_CLIENT_SECRET+"&v=20160913")
    Call<CallBackResult> getVenueById(@Path("venue_id") String venueId);


}
