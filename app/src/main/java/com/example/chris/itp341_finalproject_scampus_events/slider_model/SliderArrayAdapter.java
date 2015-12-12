package com.example.chris.itp341_finalproject_scampus_events.slider_model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.itp341_finalproject_scampus_events.R;

import java.util.ArrayList;

/**
 * Created by Chris on 12/9/2015.
 */
public class SliderArrayAdapter extends ArrayAdapter<SliderCategory> {
    ImageView imageView;
    TextView textView;
    Context context;

    public SliderArrayAdapter(Context context, ArrayList<SliderCategory> sliderCategories){
        super(context, 0, sliderCategories);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SliderCategory category = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.drawer_custom_adapter_view,
                    parent,
                    false);
        }

        imageView = (ImageView) convertView.findViewById(R.id.imageViewSlider);
        textView = (TextView) convertView.findViewById(R.id.textViewSlider);

        textView.setText(category.getCategory());
        imageView.setImageDrawable(category.getSliderImage());

        return convertView;
    }
}
