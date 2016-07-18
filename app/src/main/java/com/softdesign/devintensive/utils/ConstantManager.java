package com.softdesign.devintensive.utils;

import android.content.Intent;

public interface ConstantManager {
    String TAG_PREFIX = "DEV ";
    String COLOR_MODE_KEY = "COLOR_MODE_KEY";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";
    String USER_PHONE_KEY = "USER_PHONE_KEY";
    String USER_MAIL_KEY = "USER_MAIL_KEY";
    String USER_VK_KEY = "USER_VK_KEY";
    String USER_GIT_KEY = "USER_GIT_KEY";
    String USER_BIO_KEY = "USER_BIO_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE="USER_CODE_LINES_VALUE";
    String USER_PROJECT_VALUE = "USER_PROJECT_VALUE";
    String USER_FIO_KEY = "USER_FIO_KEY";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    String PARCELABLE_KEY = "PARCELABLE_KEY";
    String LAST_UPDATE = "LAST_UPDATE";

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
}
