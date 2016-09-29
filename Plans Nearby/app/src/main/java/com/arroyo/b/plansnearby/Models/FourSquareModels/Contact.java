package com.arroyo.b.plansnearby.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Contact {


    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("formattedPhone")
    @Expose
    private String formattedPhone;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("facebookUsername")
    @Expose
    private String facebookUsername;
    @SerializedName("facebookName")
    @Expose
    private String facebookName;

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    /**
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The formattedPhone
     */
    public String getFormattedPhone() {
        return formattedPhone;
    }

    /**
     * @param formattedPhone The formattedPhone
     */
    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

}

