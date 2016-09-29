package com.arroyo.b.plansnearby.Models.EventBriteModels;

/**
 * Created by nolbertoarroyo on 8/18/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Events {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("events")
    @Expose
    private List<Event> events = new ArrayList<Event>();

    /**
     *
     * @return
     * The pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     *
     * @param pagination
     * The pagination
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     *
     * @return
     * The events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     *
     * @param events
     * The events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

}