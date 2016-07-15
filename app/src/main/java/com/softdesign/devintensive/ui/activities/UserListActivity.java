package com.softdesign.devintensive.ui.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.User;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.fragments.RetainedFragment;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {


    public static final String TAG = ConstantManager.TAG_PREFIX + " User List Activity";

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;
    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private RetainedFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgress();
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        if (savedInstanceState == null) {
        } else {
        }

        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("users");
        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "users").commit();

            dataFragment.setData(initUsers());
            Log.w(TAG, "onCreate: users size "+ dataFragment.getData().size());
        }
        Log.w(TAG, "onCreate: users size not first"+ dataFragment.getData().size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setupToolbar();
        setupDrawer();
        loadUsers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    private List<UserDTO> initUsers() {

        Call<UserListRes> call = mDataManager.getUserList();
        final List<UserDTO> tempUsers = new ArrayList<>();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {

                        List<User> users = response.body().getData();
                        for (User user : users) {

                            tempUsers.add(new UserDTO(user));

                        }
                        Log.w(TAG, "onResponse: "+tempUsers.size() );
                        dataFragment.setData(tempUsers);
                        loadUsers();
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                    showSnackbar("Что-то пошло не так");
                }

            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
//// TODO: 14.07.2016 обработать ошибок
                showSnackbar("Что-то случилось...");
                Log.e(TAG, "onFailure: " + t.getMessage());
                finish();
            }
        });
        Log.w(TAG, "initUsers(): " + tempUsers.size());
    return tempUsers;
    }

    private void loadUsers() {

        mUsersAdapter = new UsersAdapter(dataFragment.getData(), new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClick(int position) {
                showSnackbar("Пользователь с индексом " + position);
                UserDTO userDTO = dataFragment.getData().get(position);
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                startActivity(profileIntent);
            }
        });
        mRecyclerView.setAdapter(mUsersAdapter);

    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View view = navigationView.getHeaderView(0);
        final ImageView imageView = (ImageView) view.findViewById(R.id.avatar);
        Glide.with(this)
                .load(mDataManager.getPreferencesManager().loadAvatarPhoto())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ava)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(UserListActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.user_profile_menu:
                        Intent toUserList = new Intent(UserListActivity.this, MainActivity.class);
                        startActivity(toUserList);
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        dataFragment.setData(dataFragment.getData());
        super.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<UserDTO> users = new ArrayList<>();
        for (UserDTO user : dataFragment.getData()) {
            if (user.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                users.add(user);
            }
        }
        mUsersAdapter.setUsers(users);
        mUsersAdapter.notifyDataSetChanged();
        return true;
    }

}
