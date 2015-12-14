package com.example.chris.itp341_finalproject_scampus_events.event_create_fragments;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chris.itp341_finalproject_scampus_events.EventCreateActivity;
import com.example.chris.itp341_finalproject_scampus_events.FetchAddressIntentService;
import com.example.chris.itp341_finalproject_scampus_events.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Chris on 12/12/2015.
 */
public class EditLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {
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
    EditText editTextAddressQuery;
    ImageButton imageButtonSearch;
    String addressQuery = null;
    final int MAP_ZOOM_LEVEL = 16;

    EventCreateActivity activity;
    final private String TAG = getClass().getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.event_edit_location_view, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (EventCreateActivity) getActivity();
        activity.editLocationFragment = this;

        findAllViewsById();
        buildGoogleApiClient();
    }

    private void findAllViewsById(){
        mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.editMap);
        editTextAddressQuery = (EditText) activity.findViewById(R.id.edit_text_address_search);
        imageButtonSearch = (ImageButton) activity.findViewById(R.id.btn_search);

        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("String","Attempting to search for address");
                addressQuery = editTextAddressQuery.getText().toString();
                mGoogleApiClient.reconnect();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        Log.d("String","Lat: "+mLatLng.latitude+", Lng: "+mLatLng.longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLng, MAP_ZOOM_LEVEL);

        map.clear();
        map.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(mAddressOutput));

        map.animateCamera(cameraUpdate);

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

                Location tempLocation = resultData.getParcelable(FetchAddressIntentService.FIND_LOCATION_DATA_EXTRA);


                if(tempLocation != null) {
                    Log.d("String","FOund bundled location");
                    mLastLocation = tempLocation;
                }
                else {
                    Log.d("String","Didnt find bundled Location");

                }
                activity.eventLocation = mLastLocation;
                activity.textLocation = mAddressOutput;

                Log.d("String","Final Address: "+ mAddressOutput);
                loadMapLocation();
            }

        }
    }

    private void loadMapLocation(){
        mapFragment.getMapAsync(this);

    }

    protected void startIntentService() {
        mResultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(activity, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
        activity.startService(intent);
    }

    protected void startSearchIntentService(){
        mResultReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(activity, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(FetchAddressIntentService.FIND_LOCATION_DATA_EXTRA, addressQuery);
        activity.startService(intent);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to Google Play services");
        // Connected to Google Play services!
        // The good stuff goes here.

        //GET CURRENT LOCATION HERE
        if(addressQuery == null || addressQuery.equals("")){
            mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                startIntentService();
            }
        }else {
            startSearchIntentService();
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
                result.startResolutionForResult(activity, 0);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            Toast.makeText(activity, "Error: " + result.getErrorCode(), Toast.LENGTH_LONG).show();
            mResolvingError = true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.location.LocationServices.API)
                .build();

        Log.d(TAG, "Built Google Api Client");
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
