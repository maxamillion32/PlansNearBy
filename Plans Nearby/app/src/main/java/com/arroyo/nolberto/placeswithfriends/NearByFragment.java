package com.arroyo.nolberto.placeswithfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class NearByFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView placeName;
    private TextView placeAddress;
    private TextView googleAttributions;
    private GoogleApiClient googleApiClient;
    private ImageView placeImage;
    private TextView placeRating;
    private TextView placeCategory;
    private RatingBar placeRatingBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(),this)
                .build();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nearby,container,false);
        placeName = (TextView) root.findViewById(R.id.nearby_frag_name);
        placeAddress = (TextView) root.findViewById(R.id.nearby_frag_address);
        googleAttributions = (TextView) root.findViewById(R.id.nearby_frag_attributions);
        Button pickerButton = (Button) root.findViewById(R.id.pickerButton);
        placeCategory = (TextView) root.findViewById(R.id.nearby_frag_category);
        placeRating = (TextView)root.findViewById(R.id.nearby_frag_rating);
        placeRatingBar = (RatingBar) root.findViewById(R.id.ratingBar);

        placeImage = (ImageView)root.findViewById(R.id.nearby_frag_image);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(getContext(), data);
            String placeId = place.getId();
            placePhotosAsync(placeId);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            place.getPhoneNumber();
            place.getPriceLevel();
            place.getWebsiteUri();
            String category = String.valueOf(place.getPlaceTypes());
            float rating = place.getRating();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }
            placeRatingBar.setRating(rating);
            placeName.setText(name);
            placeAddress.setText(address);
            googleAttributions.setText(Html.fromHtml(attributions));
            placeCategory.setText(category);
            placeRating.setText(rating +" ");

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }
            placeImage.setImageBitmap(placePhotoResult.getBitmap());
        }
    };

    /**
     * Load a bitmap from the photos API asynchronously
     * by using buffers and result callbacks.
     */
    private void placePhotosAsync(String id) {

        Places.GeoDataApi.getPlacePhotos(googleApiClient, id)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {


                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(googleApiClient, placeImage.getWidth(),
                                            placeImage.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }else{
                            placeImage.setImageResource(R.drawable.no_images);
                        }
                        photoMetadataBuffer.release();
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

}

