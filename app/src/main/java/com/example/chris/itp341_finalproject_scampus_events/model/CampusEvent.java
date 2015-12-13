package com.example.chris.itp341_finalproject_scampus_events.model;

import android.graphics.Bitmap;
import android.location.Address;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Chris on 12/7/2015.
 */
public class CampusEvent implements Serializable {
    private String eventTitle;
    private String eventDescription;
    private Address eventAddress;

    private Calendar startDate = null;
    private Calendar endDate = null;

    private Bitmap bitmapImage;

    public CampusEvent(String eventTitle, Address eventAddress) {
        this.eventTitle = eventTitle;
        this.eventAddress = eventAddress;
    }

    public CampusEvent() {

    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
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

    public Address getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(Address eventAddress) {
        this.eventAddress = eventAddress;
    }
}
