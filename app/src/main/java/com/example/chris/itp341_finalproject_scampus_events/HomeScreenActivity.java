package com.example.chris.itp341_finalproject_scampus_events;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chris.itp341_finalproject_scampus_events.model.CampusEvent;
import com.example.chris.itp341_finalproject_scampus_events.model.CampusEventSingleton;
import com.example.chris.itp341_finalproject_scampus_events.model.EventArrayAdapter;
import com.example.chris.itp341_finalproject_scampus_events.parse_files.SignupActivity;
import com.example.chris.itp341_finalproject_scampus_events.slider_model.SliderArrayAdapter;
import com.example.chris.itp341_finalproject_scampus_events.slider_model.SliderCategory;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Chris on 12/8/2015.
 */
public class HomeScreenActivity extends AppCompatActivity {
    //UI VARIABLES
    ListView eventListView;
    EventArrayAdapter arrayAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    //SLIDING DRAWER VARIABLES
    private ListView mDrawerList;
    private SliderArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private TextView mUsername;

    //DATABASE VARIABLES
    ParseUser user;

    //OTHER VARIABLES
    private static final int EVENT_CREATE_ACTIVITY = 0;
    private static final int EVENT_DETAIL_ACTIVITY = 1;
    public static final String TAG = "HomeScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_view);

        user = ParseUser.getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        findAllViews();
        setListenersAndAdapters();
        addDrawerItems();
        setupDrawer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EVENT_CREATE_ACTIVITY) {
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawerItems() {
        ArrayList<SliderCategory> arrCategories = new ArrayList<>();
        SliderCategory category = new SliderCategory("Create Event",getDrawable(R.drawable.ic_add_black_18dp));
        SliderCategory category2 = new SliderCategory("Log Off", getDrawable(R.drawable.ic_power_settings_new_black_18dp));

        arrCategories.add(category);
        arrCategories.add(category2);

        mAdapter = new SliderArrayAdapter(this, arrCategories);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textCategory = (TextView) view.findViewById(R.id.textViewSlider);

                switch (textCategory.getText().toString()){
                    case "Create Event":
                        Intent intentCreateEvent = new Intent(getApplicationContext(), EventCreateActivity.class);
                        startActivityForResult(intentCreateEvent, EVENT_CREATE_ACTIVITY);
                        break;
                    case "Log Off":
                        ParseUser.logOutInBackground();
                        finish();
                        break;
                    default:

                }
            }
        });

    }

    private  void setListenersAndAdapters(){
        //Main UI
        arrayAdapter = new EventArrayAdapter(this, CampusEventSingleton.getInstance(this).getCampusEvents());
        eventListView.setAdapter(arrayAdapter);
        CampusEventSingleton.getInstance(this).setAdapter(arrayAdapter);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentEventDetail = new Intent(getApplicationContext(), EventDetailActivity.class);
                intentEventDetail.putExtra(EventDetailActivity.EVENT_EXTRA, position);
                startActivity(intentEventDetail);
            }
        });

    }

    private void findAllViews(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(R.color.gold,
                R.color.ColorPrimary,
                R.color.ColorPrimaryDark);
        eventListView = (ListView) findViewById(R.id.listview_events);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RefreshTask().execute();
            }
        });

        //Sliding Drawer UI
        mUsername = (TextView) findViewById(R.id.drawer_name_header);
        mUsername.setText(user.getString(SignupActivity.FULLNAME));
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
    }

    private class RefreshTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            CampusEventSingleton.getInstance(getApplicationContext()).getCampusEvents();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            swipeRefreshLayout.setRefreshing(aBoolean);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
