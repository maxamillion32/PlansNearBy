package com.arroyo.b.plansnearby.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/13/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecialItem {

    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("displayValue")
    @Expose
    private String displayValue;

    /**
     *
     * @return
     * The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     * The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @return
     * The displayValue
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     *
     * @param displayValue
     * The displayValue
     */
    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

}
