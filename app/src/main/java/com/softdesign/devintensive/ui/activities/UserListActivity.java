package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.User;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

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
    private List<User> mUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        mDataManager=DataManager.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setupToolbar();
        setupDrawer();
        loadUsers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadUsers() {
        Call<UserListRes> call = mDataManager.getUserList();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if(response.code()==200){
                        mUsers = response.body().getData();
                        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                            @Override
                            public void onUserItemClick(int position) {
                                showSnackBar("Пользователь с индексом "+ position);
                                UserDTO userDTO = new UserDTO(mUsers.get(position));
                                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY,userDTO);
                                startActivity(profileIntent);
                            }
                        });
                        mRecyclerView.setAdapter(mUsersAdapter);
                    }
                }catch (NullPointerException e){
                    Log.e(TAG,e.getMessage());
                    showSnackBar("Что-то пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
//// TODO: 14.07.2016 обработать ошибок
            }
        });
    }
    private void showSnackBar(String message){
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.user_profile_menu:
                        Intent toUserList = new Intent(UserListActivity.this, MainActivity.class);
                        startActivity(toUserList);
                }
                return false;
            }
        });

    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.teams_title);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
