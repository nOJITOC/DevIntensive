package com.softdesign.devintensive.utils;

import android.content.Intent;

public interface ConstantManager {
    String TAG_PREFIX = "DEV ";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String PARCELABLE_KEY = "PARCELABLE_KEY";
    String LAST_UPDATE = "LAST_UPDATE";
    String MAIN_USER = "MAIN_USER";

    String[] USER_INTENT_ACTION = {
            Intent.ACTION_DIAL,
            Intent.ACTION_SENDTO,
            Intent.ACTION_DEFAULT,
            Intent.ACTION_DEFAULT

    };
    String[] INTENT_SCHEME = {
            "tel:",
            "mailto:                                                                                                                                                                                                                                                                                                            ",
            "https://",
            "https://"
    };
    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 88;
    int PERMISSION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    float BEHAVIOR_MULTIPIER = 112.0f / 256f;
    int SEARCH_DELAY = 1500;
    int RUN_DELAY = 1500;
}
