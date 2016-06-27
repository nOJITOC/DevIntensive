package com.softdesign.devintensive.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.MyBehavior;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrentEditMode = 0;

    private DataManager mDataManager;
    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVK, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;
    private MyBehavior mBehavior;
    private ImageView mImageView;
    LinearLayout mLayout;



    @Override
    /**
     * @param savedInstanceState - значения в Bundle - состояние UI
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();
        mCallImg = (ImageView) findViewById(R.id.call_img);
        mCallImg.setOnClickListener(this);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVK = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.github_et);
        mUserBio = (EditText) findViewById(R.id.bio_et);
        mUserInfoViews = new ArrayList();
        mUserInfoViews.add(mUserBio);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVK);
//        mBehavior = new MyBehavior();
      //  mImageView=(ImageView)findViewById(R.id.main_image);
       // mLayout=(LinearLayout) findViewById(R.id.scroll_layout);
//        if(mBehavior.layoutDependsOn(mCoordinatorLayout,mLayout,mImageView)){
//            mBehavior.onDependentViewChanged(mCoordinatorLayout,mLayout,mImageView);
//        }



        mFab.setOnClickListener(this);

        Log.d(TAG, "onCreate");
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();

        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserInfoValue();


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");

    }

    @Override
    public void onClick(View view) {

        Log.d(TAG, "onClick");
        switch (view.getId()) {
            case R.id.fab:
                if (mCurrentEditMode == 1) {
                    mCurrentEditMode = 0;
                } else if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                }
                changeEditMode(mCurrentEditMode);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerHeader(navigationView,BitmapFactory.decodeResource(getResources(),
                R.drawable.ava),"Марухин Михаил","mmteams91java@gmail.com");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
    private void setupDrawerHeader(NavigationView parent, Bitmap avatar, String name, String email){
        View view = parent.getHeaderView(0);
        if (avatar != null) {
            RoundedBitmapDrawable dr=RoundedBitmapDrawableFactory.create(getResources(),avatar);
            dr.setCircular(true);
            ImageView imageView = (ImageView)view.findViewById(R.id.avatar);
            imageView.setImageDrawable(dr);
        }
        if (name != null){
            TextView textView = (TextView)view.findViewById(R.id.user_name_txt);
            textView.setText(name);
        }
        if (email != null){
            TextView textView = (TextView)view.findViewById(R.id.user_email_txt);
            textView.setText(email);
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START))
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    /**
     * переключение режима редактирования
     *
     * @param mode 1-рeдактирование 0 - просмотр
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserInfoValue();
            }
        }
    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userInfoView : mUserInfoViews) {
            userData.add(userInfoView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }


}
