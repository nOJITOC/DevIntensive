package com.softdesign.devintensive.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.softdesign.devintensive.data.storage.models.MainUserDTO;

/**
 * Created by Иван on 19.07.2016.
 */
public class MainFragment extends Fragment {
        // data object we want to retain
        private MainUserDTO mUser;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }

    public void setData(MainUserDTO data) {
        this.mUser = data;
    }

        public MainUserDTO getData() {
            return mUser;
        }
}
