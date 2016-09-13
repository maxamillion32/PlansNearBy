package com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("reasons")
    @Expose
    private Reasons reasons;
    @SerializedName("venue")
    @Expose
    private Venue venue;
    @SerializedName("referralId")
    @Expose
    private String referralId;
    @SerializedName("tips")
    @Expose
    private List<Tip> tips = new ArrayList<Tip>();

    /**
     *
     * @return
     * The reasons
     */
    public Reasons getReasons() {
        return reasons;
    }

    /**
     *
     * @param reasons
     * The reasons
     */
    public void setReasons(Reasons reasons) {
        this.reasons = reasons;
    }

    /**
     *
     * @return
     * The venue
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     *
     * @param venue
     * The venue
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    /**
     *
     * @return
     * The referralId
     */
    public String getReferralId() {
        return referralId;
    }

    /**
     *
     * @param referralId
     * The referralId
     */
    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

    /**
     *
     * @return
     * The tips
     */
    public List<Tip> getTips() {
        return tips;
    }

    /**
     *
     * @param tips
     * The tips
     */
    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

}
