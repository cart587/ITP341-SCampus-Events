package com.example.chris.itp341_finalproject_scampus_events.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.vision.barcode.Barcode;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Chris on 12/8/2015.
 */
public class CampusEventSingleton {
    ArrayList<CampusEvent> mEvents;
    CampusEvent dbCampusEvent;

    //PARSE QUERIES
    public static String QUERY_TEXT_LOC = "textLocation";
    public static String QUERY_CAMPUS_EVENT = "CampusEvent";
    public static String QUERY_EVENT_TITLE = "eventTitle";
    public static String QUERY_EVENT_DESC = "eventDescription";
    public static String QUERY_EVENT_LOC = "eventLocation";
    public static String QUERY_START_DATE = "startDate";
    public static String QUERY_END_DATE = "endDate";
    public static String QUERY_BITMAP = "imageFile";

    private static CampusEventSingleton campusEventSingleton;
    private Context context;
    private ArrayAdapter arrayAdapter;

    private CampusEventSingleton(Context c){
        this.context = c;
        mEvents = new ArrayList<>();


    }

    public static CampusEventSingleton getInstance(Context c){
        if(campusEventSingleton == null)
            campusEventSingleton = new CampusEventSingleton(c.getApplicationContext());
        return campusEventSingleton;
    }

    public void setAdapter(ArrayAdapter<CampusEvent> arrayAdapter){
        this.arrayAdapter = arrayAdapter;
    }

    public ArrayList<CampusEvent> getCampusEvents(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(QUERY_CAMPUS_EVENT);
        mEvents.clear();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects) {
                    CampusEvent dbCampusEvent = new CampusEvent();

                    //get uniqueId
                    dbCampusEvent.setUniqueId(object.getObjectId());

                    //get title
                    dbCampusEvent.setEventTitle(object.getString(QUERY_EVENT_TITLE));

                    //get description
                    dbCampusEvent.setEventDescription(object.getString(QUERY_EVENT_DESC));

                    //get dates
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(object.getDate(QUERY_START_DATE));
                    dbCampusEvent.setStartDate(startCal);

                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(object.getDate(QUERY_END_DATE));
                    dbCampusEvent.setEndDate(endCal);

                    //get location
                    ParseGeoPoint geoLocation = object.getParseGeoPoint(QUERY_EVENT_LOC);
                    Location eventLocation = new Location("provider");
                    eventLocation.setLatitude(geoLocation.getLatitude());
                    eventLocation.setLongitude(geoLocation.getLongitude());
                    dbCampusEvent.setLocation(eventLocation);
                    dbCampusEvent.setTextLocation(object.getString(QUERY_TEXT_LOC));

                    Log.d("String", "Should come first");
                    mEvents.add(dbCampusEvent);
                }
                arrayAdapter.notifyDataSetChanged();

            }
        });
        Log.d("String","I shouldn't come first");
        return mEvents;
    }

    public CampusEvent getCampusEvent(int position){
        return mEvents.get(position);
    }

    public CampusEvent getCampusEvent(String objectId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(QUERY_CAMPUS_EVENT);


        GetCallback<ParseObject> singleCallBack = new getSingleCallBack();
        query.getInBackground(objectId,singleCallBack) ;

        return dbCampusEvent;
    }

    class getSingleCallBack implements GetCallback<ParseObject> {

        @Override
        public void done(ParseObject object, ParseException e) {
            //get uniqueId
            dbCampusEvent.setUniqueId(object.getObjectId());

            //get title
            dbCampusEvent.setEventTitle(object.getString(QUERY_EVENT_TITLE));

            //get description
            dbCampusEvent.setEventDescription(object.getString(QUERY_EVENT_DESC));

            //get dates
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(object.getDate(QUERY_START_DATE));
            dbCampusEvent.setStartDate(startCal);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(object.getDate(QUERY_END_DATE));
            dbCampusEvent.setEndDate(endCal);

            //get location
            Location address = (Location) object.get(QUERY_EVENT_LOC);
            dbCampusEvent.setLocation(address);
            dbCampusEvent.setTextLocation(object.getString(QUERY_TEXT_LOC));

            //get image
            ParseFile imageFile = object.getParseFile(QUERY_BITMAP);
            try {
                byte[] byteArray = imageFile.getData();
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                dbCampusEvent.setBitmapImage(bmp);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            Log.d("String", "Returning Single CampusEvent item from DB");
        }
    }

    public void addCampusEvent(CampusEvent ce){
        ParseGeoPoint geoPoint = new ParseGeoPoint(ce.getLocation().getLatitude(), ce.getLocation().getLongitude());

        ParseObject addObject = new ParseObject(QUERY_CAMPUS_EVENT);
        addObject.put(QUERY_EVENT_TITLE, ce.getEventTitle());
        addObject.put(QUERY_EVENT_DESC, ce.getEventDescription());
        addObject.put(QUERY_TEXT_LOC,ce.getTextLocation());
        addObject.put(QUERY_EVENT_LOC, geoPoint);
        addObject.put(QUERY_START_DATE,ce.getStartDate().getTime());
        addObject.put(QUERY_END_DATE, ce.getEndDate().getTime());

        if(ce.getBitmapImage() != null){
            Bitmap bitmap = ce.getBitmapImage();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] imageData = stream.toByteArray();

            ParseFile image = new ParseFile("eventImage.png", imageData);
            addObject.put(QUERY_BITMAP,image);
        }

        addObject.saveInBackground();
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
