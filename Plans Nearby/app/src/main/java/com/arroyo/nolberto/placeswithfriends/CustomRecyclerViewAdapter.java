package com.arroyo.nolberto.placeswithfriends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

/**
 * Created by nolbertoarroyo on 8/19/16.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>{
    private ArrayList<Place> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemImage;
    public TextView itemTitle;
    public TextView itemCategory;
    public TextView distanceMiles;

    public ViewHolder(View itemView) {
        super(itemView);
        itemImage = (ImageView) itemView.findViewById(R.id.list_item_image);
        itemTitle = (TextView) itemView.findViewById(R.id.list_item_title);
        itemCategory = (TextView) itemView.findViewById(R.id.list_item_category);
        distanceMiles = (TextView) itemView.findViewById(R.id.list_item_distance);
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
    public CustomRecyclerViewAdapter(ArrayList<Place> inComingData) {
        if (inComingData != null){
            // if there is incoming data, use it
            this.data = inComingData;
        } else {
            // if there is no incoming data, make an empty list to avoid NullPointerExceptions
            this.data = new ArrayList<Place>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get context from parent ViewGroup
        Context context = parent.getContext();

        // Get layoutInflater, which will inflate our custom list item layout for us
        LayoutInflater inflater = LayoutInflater.from(context);

        View listItemLayout = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new SampleViewHolder instance
        ViewHolder viewHolder = new ViewHolder(listItemLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place dataItem = data.get(position);
        ImageView itemImage = holder.itemImage;
        TextView itemTitle = holder.itemTitle;
        TextView itemCategory = holder.itemCategory;
        TextView distanceMiles = holder.itemCategory;

        //populate the views with dataItem properties
        itemTitle.setText(dataItem.getName());
        distanceMiles.setText(dataItem.getAddress());
        itemCategory.setText(String.valueOf(dataItem.getPlaceTypes()));



    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}