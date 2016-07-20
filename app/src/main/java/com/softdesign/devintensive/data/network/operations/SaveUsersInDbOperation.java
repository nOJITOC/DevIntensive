package com.softdesign.devintensive.data.network.operations;

import android.support.annotation.NonNull;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Иван on 20.07.2016.
 */
public class SaveUsersInDbOperation  extends ChronosOperation<List<User>> {
    private Boolean mDoRefresh =false;
    private List<User> mUsers;

    public SaveUsersInDbOperation(List<User> users, Boolean doRefresh) {
        mUsers = users;
        mDoRefresh = doRefresh;
    }

    @Nullable
    @Override
    //Chronos will run this method in a background thread, which means you can put
    //any time-consuming calls here, as it will not affect UI thread performance
    public List<User> run() {
        DataManager dataManager = DataManager.getInstance();
        UserDao userDao = dataManager.getDaoSession().getUserDao();
        RepositoryDao repositoryDao = dataManager.getDaoSession().getRepositoryDao();
        List<Repository> repositories = new ArrayList<Repository>();
        List<User> allUsers = new ArrayList<>();


        if(mDoRefresh) {
            userDao.deleteAll();
            repositoryDao.deleteAll();
        }


        System.out.println();
        userDao.insertOrReplaceInTx(allUsers);
        repositoryDao.insertOrReplaceInTx(repositories);
        return mUsers;
    }


    // To be able to distinguish results from different operations in one Chronos client
    // (most commonly an activity, or a fragment)
    // you should create an 'OperationResult<>' subclass in each operation,
    // so that it will be used as a parameter
    // in a callback method 'onOperationFinished'
    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }
    // the class is a named version of ChronosOperationResult<> generic class
    // it is required because Java disallows method overriding by using generic class with another parameter
    // and result delivery is based on calling particular methods with the exact same result class
    // later we'll see how Chronos use this class
    public final static class Result extends ChronosOperationResult<List<User>> {
        // usually this class is empty, but you may add some methods to customize its behavior
        // however, it must have a public constructor with no arguments
    }
}
