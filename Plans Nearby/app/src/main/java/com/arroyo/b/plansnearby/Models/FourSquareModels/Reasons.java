package com.arroyo.b.plansnearby.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reasons {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<ItemSummary> items = new ArrayList<ItemSummary>();

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The items
     */
    public List<ItemSummary> getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(List<ItemSummary> items) {
        this.items = items;
    }

}
