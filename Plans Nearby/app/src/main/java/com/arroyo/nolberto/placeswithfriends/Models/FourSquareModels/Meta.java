package com.arroyo.nolberto.placeswithfriends.Models.FourSquareModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nolbertoarroyo on 9/8/16.
 */
public class Meta {


    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("requestId")
    @Expose
    private String requestId;

    /**
     * @return The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return The requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @param requestId The requestId
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}

