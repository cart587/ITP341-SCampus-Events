package com.example.chris.itp341_finalproject_scampus_events.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chris on 12/8/2015.
 */
public class CampusEventSingleton {
    ArrayList<CampusEvent> mEvents;

    private static CampusEventSingleton campusEventSingleton;
    private Context context;

    private CampusEventSingleton(Context c){
        this.context = c;
        mEvents = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            mEvents.add(new CampusEvent("USC Church Night","1027 W. 34th St, Los Angeles, CA","9:00pm"));
        }

    }

    public static CampusEventSingleton getInstance(Context c){
        if(campusEventSingleton == null)
            campusEventSingleton = new CampusEventSingleton(c.getApplicationContext());
        return campusEventSingleton;
    }

    public ArrayList<CampusEvent> getCampusEvents(){
        return mEvents;
    }

    public CampusEvent getCampusEvent(int index){
        return mEvents.get(index);
    }

    public void addCampusEvent(CampusEvent ce){
        mEvents.add(ce);
    }

    public void updateCampusEvent(int position, CampusEvent s){
        if(position >= 0 && position < mEvents.size()) {
            mEvents.set(position, s);
        }

    }

    public void removeCampusEvent(int position){
        if(position >= 0 && position < mEvents.size())
            mEvents.remove(position);
    }

    public boolean saveCampusEvents(){
        try{
            return true;
        }catch (Exception e){

        }
        return false;
    }

}
