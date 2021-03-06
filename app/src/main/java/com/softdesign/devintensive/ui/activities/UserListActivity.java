package com.softdesign.devintensive.ui.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.operations.LoadUsersFromDbOperation;
import com.softdesign.devintensive.data.network.operations.LoadUsersFromDbOperationByQuery;
import com.softdesign.devintensive.data.network.operations.SaveUsersInDbOperation;
import com.softdesign.devintensive.data.network.res.UserData;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.MainUserDTO;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.ui.fragments.RetainedFragment;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.eventbus.ChargingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity {


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
    private Handler mHandler;
    private UsersAdapter.UserViewHolder.CustomClickListener mCustomClickListener;
    MainUserDTO mMainUser;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;
    private EventBus mBus;
    private ProgressDialog pd;
    private ChronosConnector mConnector;
    String mQuery="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Log.e(TAG, Thread.currentThread().getName());
        showProgress();
        mBus = EventBus.getDefault();
        mBus.register(this);
        mConnector = new ChronosConnector();
        mHandler=new Handler();
        mConnector.onCreate(this, savedInstanceState);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();
        mMainUser = mDataManager.getPreferencesManager().loadMainUser();
        FragmentManager fm = getFragmentManager();
        mCustomClickListener = new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClick(int position) {
                Log.e(TAG, "onUserItemClick: ");
                showSnackbar("Пользователь с индексом " + position);
                UserDTO user = new UserDTO(dataFragment.getData().get(position));
                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, user);
                startActivity(profileIntent);
            }
        };
        dataFragment = (RetainedFragment) fm.findFragmentByTag("users");
        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            dataFragment.setData(new ArrayList<User>());
            fm.beginTransaction().add(dataFragment, "users").commit();
            if (mDataManager.getUserListFromDb().size() == 0) {
                mBus.post(new ChargingEvent("DbInSave"));
            } else {
                showProgress();
                mBus.post(new ChargingEvent("dbFound"));
            }
        } else {
            loadUsers();
        }


        if (savedInstanceState == null) {
        } else {
            mQuery=savedInstanceState.getString(ConstantManager.QUERY);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);


        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        setupSwipe();
        setupToolbar();
        setupDrawer();
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
        outState.putString(ConstantManager.QUERY,mQuery);
        mConnector.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    private void loadUsersFromDb() {
        mConnector.runOperation(new LoadUsersFromDbOperation(), false);
    }

    /**
     * reload database by swipe
     */
    private void setupSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                refreshUsersInDb();
                mSwipeRefreshLayout.setRefreshing(false);


            }
        });
    }

    /**
     * Call server for load db and include it to native web-db
     */
    private void refreshUsersInDb() {
        pd = ProgressDialog.show(UserListActivity.this, getString(R.string.progress_dialog_up), getString(R.string.progress_dialog_mid), true, false);
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserListRes> call = mDataManager.getUserList();
            final List<UserDTO> tempUsers = new ArrayList<>();
            call.enqueue(new Callback<UserListRes>() {
                @Override
                public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                    Log.e(TAG, "onResponse: " + response.code());
                    try {
                        if (response.code() == 200) {
                            List<Repository> allRepositories = new ArrayList<Repository>();
                            List<User> allUsers = new ArrayList<User>();
                            Long pos = 1l;
                            for (UserData userData : response.body().getData()) {
                                allRepositories.addAll(getRepoListFromUser(userData));
                                allUsers.add(new User(userData, pos));
                                pos++;
                            }

//                            mUserDao.insertOrReplaceInTx(allUsers);
//                            mRepositoryDao.insertOrReplaceInTx(allRepositories);
                            mConnector.runOperation(new SaveUsersInDbOperation(allUsers, allRepositories), false);
                            loadUsersFromDb();

                        } else {
                            showSnackbar("Список пользователей не может быть получен");
                            Log.e(TAG, "onResponse: " + String.valueOf(response.errorBody().source()));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getClass() + e.getMessage());
                        showSnackbar("Что-то пошло не так");
                    }
                    try {
                        mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (pd != null) pd.hide();
                            }
                        }, ConstantManager.RUN_DELAY);
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getClass() + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UserListRes> call, Throwable t) {
                    pd = ProgressDialog.show(UserListActivity.this, getString(R.string.progress_dialog_up), getString(R.string.progress_dialog_mid), true, false);

//// TODO: 14.07.2016 обработать ошибок
                    showSnackbar("Что-то случилось...");
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pd != null) pd.hide();
                        }
                    }, ConstantManager.SEARCH_DELAY);
                    finish();
                }
            });
        } else {
            showSnackbar("");
            Log.w(TAG, "initUsers(): ");
        }

    }

    private void refreshUsersInDb(List<User> userData) {

        mUserDao.insertOrReplaceInTx(userData);

    }

    private List<Repository> getRepoListFromUser(UserData userData) {

        final String userId = userData.getId().toString();
        List<Repository> repositories = new ArrayList<>();
        for (UserData.Repo repo : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repo, userId));
        }

        return repositories;

    }

    private void loadUsers() {

        mUsersAdapter = new UsersAdapter(dataFragment.getData(), mCustomClickListener);
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
                .load(mMainUser.getAvatar())
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
    protected void onPause() {
        super.onPause();
        mConnector.onPause();
        mBus.unregister(this);

        System.out.println();
    }

    @Override
    protected void onDestroy() {
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        if(!mQuery.isEmpty()){
            searchView.onActionViewExpanded();
            searchView.setQuery(mQuery,false);
        }
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mQuery = newText;
                mHandler=new Handler();
                int delay = ConstantManager.SEARCH_DELAY;
                if(newText.isEmpty())delay=50;
                Runnable searchUsers = new Runnable() {
                    @Override
                    public void run() {

                            loadUsersFromDbByQuery(mQuery);
                    }
                };
                mHandler.removeCallbacks(searchUsers);
                mHandler.postDelayed(searchUsers, delay);

                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");


        return true;
    }



    private void loadUsersFromDbByQuery(String query) {
        mConnector.runOperation(new LoadUsersFromDbOperationByQuery(query), false);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessage(ChargingEvent event) {
        Log.i(TAG, "BusEvent");
        Log.e(TAG, Thread.currentThread().getName());

        mHandler = new Handler();
        if (event.message == "DbInSave") {
            refreshUsersInDb();
            loadUsersFromDb();
        } else if (event.message == "dbFound") {
            loadUsersFromDb();
            loadUsers();
        }
    }

    /**
     * for change position of cards and delete it
     */
    ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            final int fromPosition = viewHolder.getAdapterPosition();
            final int toPosition = target.getAdapterPosition();

            User prev = dataFragment.getData().remove(fromPosition);
            dataFragment.getData().add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
            Long fromPos = dataFragment.getData().get(fromPosition).getPosition();
            Long toPos = dataFragment.getData().get(toPosition).getPosition();


            dataFragment.getData().get(toPosition).setPosition(fromPos);
            dataFragment.getData().get(fromPosition).setPosition(toPos);
            mUsersAdapter.notifyItemMoved(fromPosition, toPosition);
            mUserDao.update(dataFragment.getData().get(toPosition));
            mUserDao.update(dataFragment.getData().get(fromPosition));


            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            User user = dataFragment.getData().remove(position);
            mUserDao.delete(user);
            mUsersAdapter.notifyDataSetChanged();
        }
    };
//_________________________Chronos region__________________________
    public void onOperationFinished(final SaveUsersInDbOperation.Result result) {
        Log.d(TAG, "onOperationFinished: ");
        if (result.isSuccessful()) {
            dataFragment.setData(result.getOutput());
            loadUsers();

        }
    }

    public void onOperationFinished(final LoadUsersFromDbOperation.Result result) {
        Log.d(TAG, "onOperationFinished: ");
        if (result.isSuccessful()) {
            dataFragment.setData(result.getOutput());
            loadUsers();

        } else {
            Log.e(TAG, "onOperationFinished: " + result.getErrorMessage());
        }
    }
    public void onOperationFinished(final LoadUsersFromDbOperationByQuery.Result result) {
        Log.d(TAG, "onOperationFinished: ");
        if (result.isSuccessful()) {
            dataFragment.setData(result.getOutput());
            loadUsers();

        } else {
            Log.e(TAG, "onOperationFinished: " + result.getErrorMessage());
        }
    }
//endregion
}
