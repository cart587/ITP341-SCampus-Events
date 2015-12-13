package com.example.chris.itp341_finalproject_scampus_events.parse_files;

import com.example.chris.itp341_finalproject_scampus_events.R;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by Chris Carter 12/13/15.
 */
public class Application extends android.app.Application {
    String APPLICATION_ID, CLIENT_KEY;

    @Override
    public void onCreate() {
        super.onCreate();

        APPLICATION_ID = getResources().getString(R.string.parse_app_id);
        CLIENT_KEY = getResources().getString(R.string.parse_client_key);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

    }
}
