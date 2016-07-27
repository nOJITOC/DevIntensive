package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UserRes;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevIntensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Class-Singleton  for {@link PreferencesManager}
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private Picasso mPicasso;

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private DaoSession mDaoSession;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevIntensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevIntensiveApplication.getDaoSession();
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;

    }

    public Context getContex() {
        return mContext;
    }

    //region =========Network===========
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserRes> loginToken(String userId) {
        return mRestService.loginToken(userId);
    }

    public Call<ResponseBody> photoToServer(String userId, MultipartBody.Part file) {
        return mRestService.photoToServer(userId, file);
    }

    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getuserList();
    }

    public Call<UserListRes> getUserList() {
        return mRestService.getuserList();
    }

    //endregion
    //============================Database===============
    public List<User> getUserListFromDb() {
        List<User> users = new ArrayList<>();
        try {
            users = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderAsc(UserDao.Properties.Id)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Long i = 0l; i < users.size(); i++) {
            users.get(i.intValue()).setPosition(i + 1);
        }
        return users;
    }

    public List<User> getUserListFromDbByPosition() {
        List<User> users = new ArrayList<>();
        try {
            users = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderAsc(UserDao.Properties.Position)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getUserListFromDbByName(String query) {
        List<User> users = new ArrayList<>();
        if (query.isEmpty()) {
            users=getUserListFromDbByPosition();
        } else {
            try {


                users = mDaoSession.queryBuilder(User.class)
                        .where(UserDao.Properties.CodeLines.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"))
                        .orderAsc(UserDao.Properties.Position)
                        .build()
                        .list();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    //endregoion
}

