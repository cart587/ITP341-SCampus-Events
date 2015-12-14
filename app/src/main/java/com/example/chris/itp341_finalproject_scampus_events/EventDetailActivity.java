package com.example.chris.itp341_finalproject_scampus_events;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.itp341_finalproject_scampus_events.model.CampusEvent;
import com.example.chris.itp341_finalproject_scampus_events.model.CampusEventSingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Chris on 12/8/2015.
 */
public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    //GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,


    //VARIABLES FOR CURRENT LOCATION
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LatLng mLatLng;
    boolean mResolvingError = false;

    //VARIABLES FOR TURNING COOR TO ADDRESS
    //private AddressResultReceiver mResultReceiver;
    ArrayList<String> addressResults;
    String mAddressOutput;

    //VARIABLES FOR MAP
    MapFragment mapFragment;
    final int MAP_ZOOM_LEVEL = 16;

    //GUI VARIABLES
    ImageView imageView;
    TextView eventTitle;
    TextView eventDescription;
    TextView textViewAddress;
    TextView textViewDate;

    //OTHER VARIABLE DECLARATIONS
    int position;
    public static final String EVENT_EXTRA = "com.example.chris.itp341_finalproject_scampus_events.event_extra";
    private final String TAG = "EventDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_view);

        findAllViewsById();
        updateGUI();
        loadMapLocation();
        //buildGoogleApiClient();
    }

    private void findAllViewsById(){
        //GET REFERENCE TO MAP
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        imageView = (ImageView) findViewById(R.id.image_view_event_detail);
        eventTitle = (TextView) findViewById(R.id.event_title_event_detail);
        eventDescription = (TextView) findViewById(R.id.event_description);
        textViewAddress = (TextView) findViewById(R.id.address_line_event_detail);
        textViewDate = (TextView) findViewById(R.id.date_event_detail);
    }

    private void updateGUI(){
        Intent intent = getIntent();
        position = intent.getIntExtra(EVENT_EXTRA,-1);

        if(position != -1) {
            CampusEvent ce = CampusEventSingleton.getInstance(this).getCampusEvent(position);

            //Image stuff
            if(ce.getBitmapImage() != null) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(ce.getBitmapImage());
            }

            //Detail Stuff
            eventTitle.setText(ce.getEventTitle());
            eventDescription.setText(ce.getEventDescription());

            //Address stuff
            mAddressOutput = ce.getTextLocation();
            mLastLocation = ce.getLocation();
            textViewAddress.setText(mAddressOutput);

            //Date stuff
            Calendar start = ce.getStartDate();
            Calendar endDate = ce.getEndDate();

                DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
                String display;
                if(endDate.get(Calendar.DAY_OF_MONTH) - Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 0) {
                    display ="midnight";
                }else{
                    display = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(endDate.getTime());
                }
                textViewDate.setText(format.format(start.getTime()) + " until " + display);
        }

    }

//    protected void startIntentService() {
//        mResultReceiver = new AddressResultReceiver(new Handler());
//        Intent intent = new Intent(this, FetchAddressIntentService.class);
//        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
//        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
//        startService(intent);
//    }

//    @Override
//    public void onConnected(Bundle connectionHint) {
//        Log.d(TAG, "Connected to Google Play services");
//        // Connected to Google Play services!
//        // The good stuff goes here.
//
//        //GET CURRENT LOCATION HERE
//        mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            startIntentService();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int cause) {
//        Log.d(TAG, "Connection suspended to Google Play");
//        // The connection has been interrupted.
//        // Disable any UI components that depend on Google APIs
//        // until onConnected() is called.
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.d(TAG, "Failed connecting to Google Play");
//        // This callback is important for handling errors that
//        // may occur while attempting to connect with Google.
//        //
//        // More about this in the 'Handle Connection Failures' section.
//
//        if (mResolvingError) {
//            // Already attempting to resolve an error.
//            return;
//        } else if (result.hasResolution()) {
//            try {
//                mResolvingError = true;
//                result.startResolutionForResult(this, 0);
//            } catch (IntentSender.SendIntentException e) {
//                // There was an error with the resolution intent. Try again.
//                mGoogleApiClient.connect();
//            }
//        } else {
//            // Show dialog using GoogleApiAvailability.getErrorDialog()
//            Toast.makeText(this, "Error: " + result.getErrorCode(),Toast.LENGTH_LONG).show();
//            mResolvingError = true;
//        }
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(com.google.android.gms.location.LocationServices.API)
//                .build();
//
//        Log.d(TAG,"Built Google Api Client");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
////        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//  //      mGoogleApiClient.disconnect();
//        super.onStop();
//    }

    @Override
    public void onMapReady(GoogleMap map) {

        mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, MAP_ZOOM_LEVEL);

        map.clear();
        map.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(mAddressOutput));

        map.animateCamera(cameraUpdate);

    }

    private void loadMapLocation(){
        mapFragment.getMapAsync(this);
    }

//    class AddressResultReceiver extends ResultReceiver {
//        public AddressResultReceiver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//            Log.d("String", "Made it to onReceiveResult");
//            // Display the address string
//            // or an error message sent from the intent service.
//            mAddressOutput= resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
//            // Show a toast message if an address was found.
//            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
//                loadMapLocation();
//            }
//
//        }
//    }
}
