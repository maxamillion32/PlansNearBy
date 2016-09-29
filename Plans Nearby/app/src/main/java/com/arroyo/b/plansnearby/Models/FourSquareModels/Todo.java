package com.arroyo.b.plansnearby.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Todo {

    @SerializedName("count")
    @Expose
    private Integer count;

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

}
