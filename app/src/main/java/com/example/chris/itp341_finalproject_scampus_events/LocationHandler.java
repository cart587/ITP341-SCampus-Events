package com.example.chris.itp341_finalproject_scampus_events;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationHandler extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    //VARIABLES FOR CURRENT LOCATION
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LatLng mLatLng;
    boolean mResolvingError = false;

    //VARIABLES FOR TURNING COOR TO ADDRESS
    private AddressResultReceiver mResultReceiver;
    String mAddressOutput;

    //VARIABLES FOR MAP
    MapFragment mapFragment;
    final int MAP_ZOOM_LEVEL = 16;

    //UI VARIABLES
    TextView longitude;
    TextView latitude;

    String TAG = "finalproject_scampus_events.LocationHandler";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViewsById();

        buildGoogleApiClient();

    }

    private void findAllViewsById(){
        longitude = (TextView) findViewById(R.id.textViewLongitude);
        latitude = (TextView) findViewById(R.id.textViewLatitude);

        //GET REFERENCE TO MAP
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, MAP_ZOOM_LEVEL);

        map.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(mAddressOutput));

        map.animateCamera(cameraUpdate);
    }

    protected void startIntentService() {
        mResultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
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
            mAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                loadMapLocation();
                Toast.makeText(getApplicationContext(), R.string.address_found, Toast.LENGTH_LONG).show();
            }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
