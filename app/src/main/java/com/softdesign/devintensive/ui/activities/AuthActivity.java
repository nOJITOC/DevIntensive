package com.softdesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserData;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UserRes;
import com.softdesign.devintensive.data.storage.models.MainUserDTO;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.eventbus.ChargingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Start activity with loginning form
 */
public class AuthActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.input_email)
    EditText mInputEmail;
    @BindView(R.id.input_password)
    EditText mInputPassword;
    @BindView(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.input_layout_password)
    TextInputLayout mInputLayoutPassword;
    @BindView(R.id.login_btn)
    Button mBtnSignUp;
    @BindView(R.id.remember_txt)
    TextView mRememberPswd;
    @BindView(R.id.main_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    private DataManager mDataManager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;
    private MainUserDTO mMainUser;
    private ProgressDialog pd = null;
    private EventBus mBus;
    private ChronosConnector mConnector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginning);
        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);



    }

    @Override
    protected void onStart() {
        this.pd = ProgressDialog.show(this, getString(R.string.progress_dialog_up),  getString(R.string.progress_dialog_mid), true, false);
        Log.d(TAG, "onStart: ");
        mBus = EventBus.getDefault();
        mBus.register(this);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        mRememberPswd.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        if(mDataManager.getPreferencesManager().getAuthToken()!=null){
            mBus.post(new ChargingEvent("auth"));
        }else{
            pd.hide();
        }
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void authBackgroundThread(ChargingEvent event){
        Log.i(TAG, "BusEvent" + Thread.currentThread().getName());
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();
        if(event.message=="auth")authByToken();
        else if(event.message=="signIn") {
            signIn();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onPause() {
        mBus.unregister(this);
        super.onPause();
        mConnector.onPause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void authByToken() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserRes> call = mDataManager.loginToken(mDataManager.getPreferencesManager().getUserId());

            call.enqueue(new Callback<UserRes>() {
                @Override
                public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                    Log.e(ConstantManager.TAG_PREFIX, "" + response.code());
                    if (response.code() == 200) {
                        loginSuccess(response.body().getData());
                        pd.hide();
                    } else {
                        showSnackbar("Введите логин и пароль");
                        pd.hide();
                    }
                }

                @Override
                public void onFailure(Call<UserRes> call, Throwable t) {
                    //TODO 11.07.2016 обработать ошибки
                    t.getStackTrace();
                    pd.hide();
                }

            });
        } else {
            toMainActivity();
            pd.hide();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                pd = ProgressDialog.show(this, getString(R.string.progress_dialog_up),  getString(R.string.progress_dialog_mid), true, false);
                mBus.post(new ChargingEvent("signIn"));
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;
        }

    }

    private void showSnackbar(String msg) {
        Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.forget_pswd)));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserData userData) {


//        String nowDate = userData.getUpdated();
//        mMainUser=mDataManager.getPreferencesManager().loadMainUser();
//        String oldDate = mDataManager.getPreferencesManager().getLastUpdateDate();
//        if (nowDate.equals(oldDate) || oldDate == null||mMainUser==null) {
            mMainUser = new MainUserDTO(userData);
            mDataManager.getPreferencesManager().saveMainUser(mMainUser);
//            mDataManager.getPreferencesManager().setLastUpdateDate(nowDate);
//        }
        toMainActivity();


    }

    private void toMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMainActivity = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(toMainActivity);
            }
        },ConstantManager.RUN_DELAY);

    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(
                    mInputEmail.getText().toString(), mInputPassword.getText().toString()));

            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    Log.e(ConstantManager.TAG_PREFIX, "" + response.code());
                    if (response.code() == 200) {
                        UserModelRes userModel = response.body();
                        showSnackbar("Добро пожаловать");
                        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
                        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUserData().getId());
                        loginSuccess(userModel.getData().getUserData());
                        pd.hide();
                    } else if (response.code() == 403) {
                        showSnackbar("Неверный логин или пароль");
                        pd.hide();

                    } else {
                        showSnackbar("Что-то пошло не так!");
                        pd.hide();


                    }

                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    //TODO 11.07.2016 обработать ошибки
                    Log.e(TAG, "onFailure: " + t.getMessage());


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.hide();
                            toMainActivity();
                        }
                    },ConstantManager.RUN_DELAY);
                }
            });
        } else {

            showSnackbar("Сеть на данный момент не доступна, пробуем загрузить предыдущие данные");
            pd.hide();
            toMainActivity();
        }

    }
}
