package com.arroyo.nolberto.placeswithfriends.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arroyo.nolberto.placeswithfriends.Activities.VenueDetailsActivity;
import com.arroyo.nolberto.placeswithfriends.DataBaseHelper;
import com.arroyo.nolberto.placeswithfriends.R;

/**
 * Created by nolbertoarroyo on 9/14/16.
 */
public class FavsFragment extends DialogFragment{
    private ListView venuesListView;
    private DataBaseHelper dbHelper;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.favs_dialog,container,false);
        venuesListView= (ListView)root.findViewById(R.id.favs_dialog_lv);
        final Cursor favsCursor = dbHelper.getInstance(getContext()).getFavoritesList();
        final CursorAdapter cursorAdapter = new CursorAdapter(getActivity(),favsCursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.favs_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                //get views
                TextView venueTitle = (TextView)view.findViewById(R.id.favs_list_item_title);
                TextView venueCategory = (TextView)view.findViewById(R.id.favs_list_item_category);
                TextView venueCity = (TextView)view.findViewById(R.id.favs_list_item_city);
                TextView venueRating = (TextView)view.findViewById(R.id.favs_list_item_rating);

                venueTitle.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_TITLE)));
                venueCategory.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_CATEGORY)));
                venueCity.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_CITY)));
                venueRating.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_RATING)));
                String ratingColor = "#"+cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_RATING_COLOR));
                venueRating.setBackgroundColor(Color.parseColor(ratingColor));
            }
        };



        venuesListView.setAdapter(cursorAdapter);
        venuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) parent.getItemAtPosition(position);
                String itemClicked = cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_ITEM_ID));
                Intent intent = new Intent(getActivity(), VenueDetailsActivity.class);
                intent.putExtra("venueId", itemClicked);
                startActivity(intent);


            }
        });
        venuesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                cursor= (Cursor)parent.getItemAtPosition(i);
                String deleteItemClicked = cursor.getString(cursor.getColumnIndex(DataBaseHelper.DataEntryFavorites.COL_ITEM_ID));
                dbHelper= DataBaseHelper.getInstance(getActivity());
                dbHelper.deleteFavoritesItem(deleteItemClicked);
                Cursor cursor1 = dbHelper.getFavoritesList();
                cursorAdapter.swapCursor(cursor1);
                cursorAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return root;
    }
}
