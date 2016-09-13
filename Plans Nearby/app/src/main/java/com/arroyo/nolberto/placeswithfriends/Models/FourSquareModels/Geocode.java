package com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geocode {

    @SerializedName("what")
    @Expose
    private String what;
    @SerializedName("where")
    @Expose
    private String where;
    @SerializedName("center")
    @Expose
    private Center center;
    @SerializedName("displayString")
    @Expose
    private String displayString;
    @SerializedName("cc")
    @Expose
    private String cc;
    @SerializedName("longId")
    @Expose
    private String longId;

    /**
     * @return The what
     */
    public String getWhat() {
        return what;
    }

    /**
     * @param what The what
     */
    public void setWhat(String what) {
        this.what = what;
    }

    /**
     * @return The where
     */
    public String getWhere() {
        return where;
    }

    /**
     * @param where The where
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * @return The center
     */
    public Center getCenter() {
        return center;
    }

    /**
     * @param center The center
     */
    public void setCenter(Center center) {
        this.center = center;
    }

    /**
     * @return The displayString
     */
    public String getDisplayString() {
        return displayString;
    }

    /**
     * @param displayString The displayString
     */
    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    /**
     * @return The cc
     */
    public String getCc() {
        return cc;
    }

    /**
     * @param cc The cc
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * @return The longId
     */
    public String getLongId() {
        return longId;
    }

    /**
     * @param longId The longId
     */
    public void setLongId(String longId) {
        this.longId = longId;
    }

}
