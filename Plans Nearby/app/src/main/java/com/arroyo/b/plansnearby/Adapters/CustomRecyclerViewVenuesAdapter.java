package com.arroyo.b.plansnearby.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arroyo.b.plansnearby.Activities.VenueDetailsActivity;
import com.arroyo.b.plansnearby.Constants;
import com.arroyo.b.plansnearby.Interfaces.ItemClickInterface;
import com.arroyo.b.plansnearby.Models.FourSquareModels.Item;
import com.arroyo.b.plansnearby.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nolbertoarroyo on 8/19/16.
 * venues recyclerView adapter
 */
public class CustomRecyclerViewVenuesAdapter extends RecyclerView.Adapter<CustomRecyclerViewVenuesAdapter.ViewHolder> {
    private ArrayList<Item> data;
    private static ItemClickInterface onVenueClickListener;
    private Context context;

    @Override
    public String toString() {
        return super.toString();
    }

    public CustomRecyclerViewVenuesAdapter(ArrayList<Item> inComingData, ItemClickInterface itemClicked) {
        this.onVenueClickListener = itemClicked;
        if (inComingData != null) {
            // if there is incoming data, use it
            this.data = inComingData;
        } else {
            // if there is no incoming data, make an empty list to avoid NullPointerExceptions
            this.data = new ArrayList<Item>();
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get context from parent ViewGroup
        context = parent.getContext();

        // Get layoutInflater, which will inflate our custom list item layout for us
        LayoutInflater inflater = LayoutInflater.from(context);

        View listItemLayout = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new SampleViewHolder instance
        ViewHolder viewHolder = new ViewHolder(listItemLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item dataItem = data.get(position);
        ImageView itemImage = holder.itemImage;
        TextView itemTitle = holder.itemTitle;
        TextView itemCategory = holder.itemCategory;
        TextView itemCity = holder.itemCity;
        TextView itemRating = holder.itemRating;
        TextView itemPriceTier = holder.itemPriceTier;
        if (dataItem.getVenue().getRating() != null) {

            //populating views with venue properties

            itemRating.setText(dataItem.getVenue().getRating().toString());
            itemRating.setTextColor(Color.WHITE);
            itemRating.setTypeface(itemRating.getTypeface(),Typeface.BOLD);
            String ratingColor = "#"+dataItem.getVenue().getRatingColor();
            itemRating.setBackgroundColor(Color.parseColor(ratingColor));
        }
        if (dataItem.getVenue().getPrice() != null) {
            Integer venueCost = dataItem.getVenue().getPrice().getTier();
            if (venueCost == 1) {

                itemPriceTier.setText("$");
            } else if (venueCost == 2) {
                itemPriceTier.setText("$$");
            } else if (venueCost == 3) {
                itemPriceTier.setText("$$$");
            } else if (venueCost == 4) {
                itemPriceTier.setText("$$$$");
            }
        }

        itemTitle.setText(dataItem.getVenue().getName());
        itemCategory.setText((CharSequence) dataItem.getVenue().getCategories().get(0).getName());
        itemCity.setText(dataItem.getVenue().getLocation().getCity());

       //set venue photo using picasso
        if (dataItem.getVenue().getFeaturedPhotos()!=null){
        String suffix = dataItem.getVenue().getFeaturedPhotos().getItems().get(0).getSuffix();
        String prefix = dataItem.getVenue().getFeaturedPhotos().getItems().get(0).getPrefix();

        String url = prefix + Constants.VENUE_IMAGE_SIZE + suffix;
        Picasso.with(context).load(url).into(holder.itemImage);
    }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle, itemCategory, itemCity, itemRating, itemPriceTier;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.list_item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_title);
            itemCategory = (TextView) itemView.findViewById(R.id.list_item_category);
            itemCity = (TextView) itemView.findViewById(R.id.list_item_distance);
            itemRating = (TextView) itemView.findViewById(R.id.list_item_price);
            itemPriceTier = (TextView) itemView.findViewById(R.id.list_item_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemClicked = data.get(getLayoutPosition()).getVenue().getId();
                    onVenueClickListener.onItemClicked(itemClicked);
                    Intent intent = new Intent(context, VenueDetailsActivity.class);
                    intent.putExtra(Constants.SELECTED_VENUE_ID_KEY, itemClicked);
                    context.startActivity(intent);
                }
            });

        }
    }
}