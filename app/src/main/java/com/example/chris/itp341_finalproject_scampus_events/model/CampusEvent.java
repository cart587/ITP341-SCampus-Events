package com.example.chris.itp341_finalproject_scampus_events.model;

/**
 * Created by Chris on 12/7/2015.
 */
public class CampusEvent {
    private String eventTitle;
    private String eventLocation;
    private String eventTime;
    private String eventDescription;

    public CampusEvent(String eventTitle, String eventLocation, String eventTime) {
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventTime = eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
