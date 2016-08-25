package com.arroyo.nolberto.placeswithfriends.Models;

/**
 * Created by nolbertoarroyo on 8/20/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Fee {

    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("major_value")
    @Expose
    private String majorValue;

    /**
     *
     * @return
     * The display
     */
    public String getDisplay() {
        return display;
    }

    /**
     *
     * @param display
     * The display
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     *
     * @return
     * The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     *
     * @return
     * The value
     */
    public Integer getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The majorValue
     */
    public String getMajorValue() {
        return majorValue;
    }

    /**
     *
     * @param majorValue
     * The major_value
     */
    public void setMajorValue(String majorValue) {
        this.majorValue = majorValue;
    }

}