package com.softdesign.devintensive.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.softdesign.devintensive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePhotoFragment extends Fragment {


    public ProfilePhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_profile_photo, container, false);
    }

    public void setPhoto(String photo){
        ImageView mProfileImage = (ImageView) getView();
        Glide.with(this)
                .load(photo)
                .fitCenter()
                .placeholder(R.drawable.nav_head_bg)
                .error(R.drawable.nav_head_bg)
                .into(mProfileImage);
    }
}
