package com.example.chris.itp341_finalproject_scampus_events.event_create_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Chris on 12/12/2015.
 */
public class EventCreatorPageAdapter extends FragmentPagerAdapter {
    CharSequence titles[];
    public int NUM_TABS = 3;
    EditDetailFragment editDetailFragment = null;
    EditEventPhotoFragment editEventPhotoFragment = null;
    EditLocationFragment editLocationFragment = null;

    public EventCreatorPageAdapter(FragmentManager fm, CharSequence[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new EditDetailFragment();
//                if(editDetailFragment == null){
//                    editDetailFragment = new EditDetailFragment();
//                }
//                return editDetailFragment;
            case 1:
                return new EditLocationFragment();
//                if(editLocationFragment == null){
//                    editLocationFragment = new EditLocationFragment();
//                }
//                return editLocationFragment;
            case 2:
                return new EditEventPhotoFragment();
//                if (editEventPhotoFragment == null) {
//                    editEventPhotoFragment = new EditEventPhotoFragment();
//                }
//                return editEventPhotoFragment;

        }
        //Should never get here
        return new EditDetailFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
