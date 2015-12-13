package com.example.chris.itp341_finalproject_scampus_events.event_create_fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chris.itp341_finalproject_scampus_events.EventCreateActivity;
import com.example.chris.itp341_finalproject_scampus_events.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Chris on 12/12/2015.
 */
public class EditEventPhotoFragment extends Fragment {
    Button btnPickPhoto;
    ImageView imageViewPhoto;
    EventCreateActivity eventCreateActivity;
    Bitmap bitMapImage = null;

    final private int RESULT_LOAD_IMAGE = 0;
    final private int CROP_PHOTO = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_photo_picker_view, null, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventCreateActivity = (EventCreateActivity) getActivity();
        eventCreateActivity.editEventPhotoFragment = this;
        findAllViewsById();
    }

    public Bitmap getEventBitMapImage() {
        return bitMapImage;
    }

    private void findAllViewsById() {
        btnPickPhoto = (Button) eventCreateActivity.findViewById(R.id.btnLoadPhoto);
        imageViewPhoto = (ImageView) eventCreateActivity.findViewById(R.id.imageViewLoadPhoto);

        btnPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Log.d("String", "Recieved something from gallery");
            Uri selectedImage = data.getData();

            try {
                bitMapImage = BitmapFactory.decodeStream(eventCreateActivity.getContentResolver().openInputStream(selectedImage));
                imageViewPhoto.setImageBitmap(bitMapImage);
            } catch (FileNotFoundException e) {
                bitMapImage = null;
                e.printStackTrace();
            }
        }
    }
}
