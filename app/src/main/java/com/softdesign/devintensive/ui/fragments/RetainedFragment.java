package com.softdesign.devintensive.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.softdesign.devintensive.data.storage.models.UserDTO;

import java.util.List;

/**
 * Created by Иван on 15.07.2016.
 */
public class RetainedFragment extends Fragment {
    // data object we want to retain
    private List<UserDTO> mUsers;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(List<UserDTO> data) {
        this.mUsers = data;
    }

    public List<UserDTO> getData() {
        return mUsers;
    }
}
