package com.arroyo.b.plansnearby.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/13/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Specials {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private List<SpecialItem> items = new ArrayList<SpecialItem>();

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
    public List<SpecialItem> getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(List<SpecialItem> items) {
        this.items = items;
    }

}