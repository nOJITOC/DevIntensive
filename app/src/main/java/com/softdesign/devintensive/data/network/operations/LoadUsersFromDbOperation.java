package com.softdesign.devintensive.data.network.operations;

import android.support.annotation.NonNull;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

/**
 * Created by Иван on 20.07.2016.
 */
public class LoadUsersFromDbOperation extends ChronosOperation<List<User>> {

    @Override
    public List<User> run() {
       return DataManager.getInstance().getUserListFromDb();
    }
    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<List<User>> {

    }
}