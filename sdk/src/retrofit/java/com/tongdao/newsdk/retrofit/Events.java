
package com.tongdao.newsdk.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Events {

    @SerializedName("events")
    @Expose
    private List<Event> events = new ArrayList<Event>();

    /**
     * 
     * @return
     *     The events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * 
     * @param events
     *     The events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
