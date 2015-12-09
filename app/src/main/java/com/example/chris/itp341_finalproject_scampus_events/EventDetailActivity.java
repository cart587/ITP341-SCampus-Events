package com.example.chris.itp341_finalproject_scampus_events;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Chris on 12/8/2015.
 */
public class EventDetailActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {
    //VARIABLES FOR CURRENT LOCATION
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LatLng mLatLng;
    boolean mResolvingError = false;

    //VARIABLES FOR TURNING COOR TO ADDRESS
    private AddressResultReceiver mResultReceiver;
    ArrayList<String> addressResults;
    String mAddressOutput;

    //VARIABLES FOR MAP
    MapFragment mapFragment;
    final int MAP_ZOOM_LEVEL = 16;

    //GUI VARIABLES
    TextView textViewAddress;
    TextView textViewDate;

    //OTHER VARIABLE DECLARATIONS
    public static final String EVENT_EXTRA = "com.example.chris.itp341_finalproject_scampus_events.event_extra";
    private final String TAG = "EventDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_view);

        findAllViewsById();
        buildGoogleApiClient();
    }

    private void findAllViewsById(){
        //GET REFERENCE TO MAP
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        textViewAddress = (TextView) findViewById(R.id.address_line_event_detail);
        textViewDate = (TextView) findViewById(R.id.date_event_detail);
    }

    private void updateGUI(){
        textViewAddress.setVisibility(View.VISIBLE);
        textViewAddress.setText(mAddressOutput);

        textViewDate.setVisibility(View.VISIBLE);
        textViewDate.setText("Today at 7:00PM");
    }

    protected void startIntentService() {
        mResultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to Google Play services");
        // Connected to Google Play services!
        // The good stuff goes here.

        //GET CURRENT LOCATION HERE
        mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            startIntentService();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection suspended to Google Play");
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "Failed connecting to Google Play");
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.

        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            Toast.makeText(this, "Error: " + result.getErrorCode(),Toast.LENGTH_LONG).show();
            mResolvingError = true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.location.LocationServices.API)
                .build();

        Log.d(TAG,"Built Google Api Client");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, MAP_ZOOM_LEVEL);

        updateGUI();
        map.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(mAddressOutput));

        map.animateCamera(cameraUpdate);

    }

    private void loadMapLocation(){
        mapFragment.getMapAsync(this);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.d("String", "Made it to onReceiveResult");
            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput= resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                loadMapLocation();
            }

        }
    }
}
