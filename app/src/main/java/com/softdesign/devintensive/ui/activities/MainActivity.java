package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.storage.models.MainUserDTO;
import com.softdesign.devintensive.ui.fragments.ProfilePhotoFragment;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.Validators.ValidateManager;
import com.softdesign.devintensive.utils.eventbus.ChargingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private int mCurrentEditMode = 0;
    /**
     * Manager of Validators {@link com.softdesign.devintensive.utils.Validators.BaseValidator} for profile info
     */
    private ValidateManager mValidateManager;
    /**
     * Manager for user profile data
     */
    private DataManager mDataManager;
    @BindView(R.id.call_img)
    ImageView mCallImg;
    /**
     * Base layout of MainActivity
     */
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindViews({R.id.phone_et, R.id.email_et, R.id.vk_et, R.id.git_placeholder, R.id.bio_et})
    List<EditText> mUserInfoViews;
    @BindViews({R.id.dev_rating_txt, R.id.dev_code_lines_txt, R.id.dev_projects_txt})
    List<TextView> mUserStatisticViews;
    @BindViews({R.id.for_phone_call, R.id.for_mail_send, R.id.for_vk_open})
    List<ImageView> mUserInfoClickers;
    @BindView(R.id.for_github_open)
    ImageView mGithubClicker;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    AppBarLayout.LayoutParams mAppBarParams = null;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    File mPhotoFile = null;
    Uri mSelectedImage = null;
    @BindView(R.id.git_spiner)
    Spinner mGitSpinner;
    MainUserDTO mMainUser;
    Integer mSpinnerPos = 0;
    private EventBus mBus = EventBus.getDefault();
    ProfilePhotoFragment  mPhotoFragment;
    FragmentManager mFragmentManager;
    @Override
    /**
     * @param savedInstanceState - значения в Bundle - состояние UI
     */
    protected void onCreate(Bundle savedInstanceState) {
        showProgress();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mBus = EventBus.getDefault();
        mBus.register(this);
        ButterKnife.bind(this);

        mBus.post(new ChargingEvent("message"));

        if (savedInstanceState == null) {
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainThread(ChargingEvent event) {
        Log.i(TAG, "BusEvent 1" + event.message);

        mDataManager = DataManager.getInstance();
        mMainUser = mDataManager.getPreferencesManager().loadMainUser();
        mFragmentManager = getSupportFragmentManager();
        mPhotoFragment=(ProfilePhotoFragment) mFragmentManager.findFragmentById(R.id.fragment_profile_image);
        mPhotoFragment.setPhoto(mMainUser.getPhoto());
        setupUserProfileContent();
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        Log.d(TAG, "onCreate");
        setupToolbar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();
        mValidateManager = new ValidateManager(this, mUserInfoViews);
    }

    /**
     * {@inheritDoc}
     */
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
        hideProgress();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        mDataManager.getPreferencesManager().saveMainUser(mMainUser);
        mBus.unregister(this);
        super.onPause();
        Log.d(TAG, "onPause");


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


        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    /**
     * @param message - текст, отображаемый на SnackBar
     */
    public void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        mCollapsingToolbar.setTitle(mMainUser.getFullName());
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
                    photoToServer(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                    Log.e(TAG, "onActivityResult: " + mPhotoFile.getName());
                    photoToServer(mSelectedImage);


                }
        }
    }


    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerHeader(navigationView, mMainUser.getFullName(), mMainUser.getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.team_menu:
                        Intent toUserList = new Intent(MainActivity.this, UserListActivity.class);
                        startActivity(toUserList);
                }
                return false;
            }
        });

    }

    /**
     * Draws an DrawerHeader
     *
     * @param parent
     * @param name   - name of user, that will be shown in head
     * @param email  - email of user, that will be shown in head
     */
    private void setupDrawerHeader(NavigationView parent, String name, String email) {
        View view = parent.getHeaderView(0);
        final ImageView imageView = (ImageView) view.findViewById(R.id.avatar);
        Glide.with(this)
                .load(mMainUser.getAvatar())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ava)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });


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
        TextView gitSelectedView = (TextView) mGitSpinner.getSelectedView();
        EditText gitET = mUserInfoViews.get(3);

        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

            }

            mFragmentManager.beginTransaction().detach(mPhotoFragment);
            mUserInfoViews.get(0).requestFocus();
            for (int i = 0; i < mUserInfoViews.size() - 1; i++) {
                mUserInfoViews.get(i).addTextChangedListener(mValidateManager.getValidator(i));
            }
            if (gitSelectedView != null && gitET != null) gitET.setText(gitSelectedView.getText());
            gitET.setVisibility(View.VISIBLE);
            mGitSpinner.setVisibility(View.GONE);
            showProfilePlaceholder(true);
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserFields();
            }
            mFragmentManager.beginTransaction().attach(mPhotoFragment);
            for (int i = 0; i < mUserInfoViews.size() - 1; i++) {
                mUserInfoViews.get(i).removeTextChangedListener(mValidateManager.getValidator(i));
            }
            NestedScrollView nested = (NestedScrollView) findViewById(R.id.nested_scroll_for_et);
            if (gitSelectedView != null && gitET != null) gitSelectedView.setText(gitET.getText());
            gitET.setVisibility(View.GONE);
            mGitSpinner.setVisibility(View.VISIBLE);
            Log.e(TAG, "changeEditMode: " + mGitSpinner.indexOfChild(gitSelectedView));
            mMainUser.getRepositories().set(mSpinnerPos, gitET.getText().toString());
            mUserInfoViews.get(0).clearFocus();
            showProfilePlaceholder(false);
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

        }
    }

    private void initUserFields() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mMainUser.getPhone());
        userFields.add(mMainUser.getEmail());
        userFields.add(mMainUser.getVk());
        userFields.add(mMainUser.getRepositories().get(0));
        userFields.add(mMainUser.getBio());
        for (int i = 0; i < userFields.size(); i++) {
            mUserInfoViews.get(i).setText(userFields.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.git_spinner_item, mMainUser.getRepositories()
        );
        adapter.setDropDownViewResource(R.layout.git_spinner_dropdown_item);
        mGitSpinner.setAdapter(adapter);
        mGitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnerPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userInfoView : mUserInfoViews) {
            userData.add(userInfoView.getText().toString());
        }
        mMainUser.setPhone(userData.get(0));
        mMainUser.setEmail(userData.get(1));
        mMainUser.setVk(userData.get(2));
        mMainUser.setBio(userData.get(4));
    }

    private void initUserInfoValue() {

        mUserStatisticViews.get(0).setText(mMainUser.getRating());
        mUserStatisticViews.get(1).setText(mMainUser.getCodeLines());
        mUserStatisticViews.get(2).setText(mMainUser.getProjects());
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

    private void photoToServer(Uri fileUri) {
        RestService service =
                ServiceGenerator.createService(RestService.class);

        File file = null;
        if (fileUri.getScheme().equals("file")) {
            file = new File(fileUri.getPath());
        }
        if (fileUri.getScheme().equals("content")) {
            file = new File(getPath(fileUri));
        }
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        // finally, execute the request
        Call<ResponseBody> call = service.photoToServer(mDataManager.getPreferencesManager().getUserId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v(ConstantManager.TAG_PREFIX + " Upload", "success" + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(ConstantManager.TAG_PREFIX + " Upload error:", t.getMessage());
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
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
        mProfilePlaceholder.setEnabled(cond);
    }

    /**
     * Maked ToolBar locked and whole sized
     *
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
                                showSnackbar(getString(R.string.user_profile_dialog_gallery));
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                showSnackbar(getString(R.string.user_profile_dialog_camera));
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                showSnackbar(getString(R.string.user_profile_dialog_cancel));
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
     *
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
     *
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {
        mPhotoFragment.setPhoto(selectedImage.toString());
        Log.e(ConstantManager.TAG_PREFIX, "" + mNavigationDrawer.getWidth() + (int) getResources().getDimension(R.dimen.size_huge_256));
        mMainUser.setPhoto(selectedImage.toString());
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Start intent from{@link ConstantManager#USER_INTENT_ACTION} with Uri from
     * {@link #mUserInfoViews} with scheme from {@link ConstantManager#INTENT_SCHEME}
     *
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
    private void setupUserProfileContent() {

        for (ImageView userInfoClicker : mUserInfoClickers) {
            userInfoClicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int choice = mUserInfoClickers.indexOf(view);
                    userProfileActions(choice);
                }
            });
        }
        mGithubClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = null;
                String action = Intent.ACTION_DEFAULT;
                TextView gitTV = (TextView) mGitSpinner.getSelectedView();
                uri = Uri.parse(ConstantManager.INTENT_SCHEME[3] + gitTV.getText());
                Intent gitBrowse = new Intent(action, uri);
                startActivity(gitBrowse);
            }
        });
    }
}
