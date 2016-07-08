package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.Validators.ValidateManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrentEditMode = 0;
    /**
     * Manager of Validators {@link com.softdesign.devintensive.utils.Validators.BaseValidator} for profile info
     */
    private ValidateManager mValidateManager;
    /**Manager for user profile data */
    private DataManager mDataManager;
    @BindView(R.id.call_img)
    ImageView mCallImg;
    /**Base layout of MainActivity*/
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindViews({R.id.phone_et, R.id.email_et, R.id.vk_et, R.id.github_et, R.id.bio_et})
    List<EditText> mUserInfoViews;
    @BindViews({R.id.for_phone_call, R.id.for_mail_send, R.id.for_vk_open, R.id.for_github_open})
    List<ImageView> mUserInfoClickers;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    AppBarLayout.LayoutParams mAppBarParams = null;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    File mPhotoFile = null;
    Uri mSelectedImage = null;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;

    @Override
    /**
     * @param savedInstanceState - значения в Bundle - состояние UI
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        mValidateManager=new ValidateManager(this,mUserInfoViews);
        mCallImg.setOnClickListener(this);
        setupUserProfileContent();
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        Log.d(TAG, "onCreate");
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.nav_head_bg)
                .into(mProfileImage);
        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    /**{@inheritDoc}*/
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
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
//            uses userProfileActions


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    /**
     *
     * @param message - текст, отображаемый на SnackBar
     */
    public void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Dispatch incoming result from other Activity(photo from camera or gallery).
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
        }
    }


    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerHeader(navigationView, BitmapFactory.decodeResource(getResources(),
                R.drawable.ava), getString(R.string.user_fio), getString(R.string.e_mail));

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

    /**
     *  Draws an DrawerHeader
     * @param parent
     * @param avatar - icon of NavigationView parent which maked circular here
     * @param name - name of user, that will be shown in head
     * @param email - email of user, that will be shown in head
     */
    private void setupDrawerHeader(NavigationView parent, Bitmap avatar, String name, String email) {
        View view = parent.getHeaderView(0);
        if (avatar != null) {
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), avatar);
            dr.setCircular(true);
            ImageView imageView = (ImageView) view.findViewById(R.id.avatar);
            imageView.setImageDrawable(dr);
        }
        if (name != null) {
            TextView textView = (TextView) view.findViewById(R.id.user_name_txt);
            textView.setText(name);
        }
        if (email != null) {
            TextView textView = (TextView) view.findViewById(R.id.user_email_txt);
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
//            showSnackbar("Режим редактирования.");
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

            }
            mUserInfoViews.get(0).requestFocus();

            showProfilePlaceholder(true);
//            lockToolbar(true);
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
//            showSnackbar("Редактирование завершено.");
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserInfoValue();
            }
            showProfilePlaceholder(false);
//            lockToolbar(false);
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

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

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);

    }

    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.println(Log.INFO, TAG, "error");
            }
            if (mPhotoFile != null) {

                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, "Для корректной работы приложения необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                    .setAction("разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 01.07.2016 тут обрабатывается разрешение (разрешение получено)
                // например вывести сообщение или обработать какой-то логикой если нужно


            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 01.07.2016 тут обрабатывается разрешение (разрешение получено)
                // например вывести сообщение или обработать какой-то логикой если нужно


            }
        }
    }

    private void showProfilePlaceholder(boolean cond) {
        if (cond) mProfilePlaceholder.setVisibility(View.VISIBLE);
        else mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Maked ToolBar locked and whole sized
     * @param cond
     */
    private void lockToolbar(boolean cond) {
        if (cond) {
            mAppBarLayout.setExpanded(true, true);
        } else {
            mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        }
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    /**
     * @param id
     * @deprecated
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
//                                showSnackbar(getString(R.string.user_profile_dialog_gallery));
                                loadPhotoFromGallery();
                                break;
                            case 1:
//                                showSnackbar(getString(R.string.user_profile_dialog_camera));
                                loadPhotoFromCamera();
                                break;
                            case 2:
//                                showSnackbar(getString(R.string.user_profile_dialog_cancel));
                                dialogInterface.cancel();
                                break;
                        }
                    }
                });

                return builder.create();

            default:
                return null;

        }

    }

    /**
     * Maked new File image with .jpg and unic  name by date of  great
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        Log.println(Log.INFO, TAG, image == null ? "null image" : "not null");
        return image;
    }

    /**
     * Insert main profile image from Uri
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        Log.e(ConstantManager.TAG_PREFIX,""+mNavigationDrawer.getWidth() +(int) getResources().getDimension(R.dimen.size_huge_256));
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Start intent from{@link ConstantManager#USER_INTENT_ACTION} with Uri from
     * {@link #mUserInfoViews} with scheme from {@link ConstantManager#INTENT_SCHEME}
     * @param numOfAction
     */
    private void userProfileActions(int numOfAction) {
        Uri uri = null;
        String action = Intent.ACTION_DEFAULT;
        Log.e(TAG, "userProfileActions: " + mUserInfoViews.get(numOfAction).getText());

        action = ConstantManager.USER_INTENT_ACTION[numOfAction];
        uri = Uri.parse(ConstantManager.INTENT_SCHEME[numOfAction] + mUserInfoViews.get(numOfAction).getText());
        Intent someInfoAction = new Intent(action, uri);

        startActivity(someInfoAction);
    }

    /**
     * Add listeners for childs of user profile content
     */
    private void setupUserProfileContent(){

        for (ImageView userInfoClicker : mUserInfoClickers) {
            userInfoClicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int choice = mUserInfoClickers.indexOf(view);
                    userProfileActions(choice);
                }
            });
        }
        for (int i = 0; i < mUserInfoViews.size()-1; i++) {
            mUserInfoViews.get(i).addTextChangedListener(mValidateManager.getValidator(i));
        }
    }
}
