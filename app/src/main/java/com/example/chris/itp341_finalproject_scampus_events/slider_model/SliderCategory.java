package com.example.chris.itp341_finalproject_scampus_events.slider_model;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Chris on 12/9/2015.
 */
public class SliderCategory {
    private String category;
    private Drawable sliderImage;

    public SliderCategory(String category, Drawable sliderImage) {
        this.category = category;
        this.sliderImage = sliderImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Drawable getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(Drawable sliderImage) {
        this.sliderImage = sliderImage;
    }
}
