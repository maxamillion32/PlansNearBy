package com.arroyo.b.plansnearby;

/**
 * Created by nolbertoarroyo on 9/16/16.
 */
public class Constants {
    public static final String FOURSQUARE_CLIENT_ID = BuildConfig.FOURSQUARE_CLIENT_ID;
    public static final String FOURSQUARE_CLIENT_SECRET = BuildConfig.FOURSQUARE_CLIENT_SECRET;
    public static final String EVENTBRITE_TOKEN = BuildConfig.EVENTBRITE_TOKEN;
    public static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;
    public static final String EVENT_BASE_URL = "https://www.eventbriteapi.com/v3/";
    public static final String FOURSQUARE_BASE_URL = "https://api.foursquare.com/v2/";

    //MainActivity
    public static final String SAVED_VENUES_FRAGMENT = "saved venues fragment";
    public static final String CITY_FRAGMENT = "city fragment";
    public static final String MAIN_DRAWER_CITY_KEY = "city";
    public static final String MAIN_DRAWER_CATEGORY_KEY = "category";
    public static final String MAIN_CATEGORY_TRENDING = "Trending";
    public static final String MAIN_CATEGORY_WEEKEND = "this_weekend";
    public static final String MAIN_CATEGORY_COFFEE = "Coffee";
    public static final String MAIN_CATEGORY_ABOUT = "About";

    //PagerAdapter
    public static final String TAB_DRINKS = "drinks";
    public static final String TAB_FOOD = "food";
    public static final String TAB_TOP_PICKS = "topPicks";
    public static final String TAB_SIGHTS = "sights";
    public static final String TAB_ALL_EVENTS = "allEvents";
    public static final String TAB_INTERESTS_EVENTS = "interests";

    //SharePreferences
    public static final String SHAREPREFS = "ShaPreferences";
    public static final String FIRST_TIME_RUN_BOOLEAN = "first";
    public static final String PREFS_INTEREST_ARRAY = "interests";

    //EventDetailsActivity
    public static final String SELECTED_EVENT_ID_KEY = "selected event id";

    //VenueDetailsActivity

    public static final String SELECTED_VENUE_ID_KEY = "selected venue id";
    public static final String VENUE_IMAGE_SIZE = "original";


}
