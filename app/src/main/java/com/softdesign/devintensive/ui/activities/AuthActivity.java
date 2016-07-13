package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.User;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.network.res.UserRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.UserInfoUpdateHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        if(mDataManager.getPreferencesManager().getAuthToken()!=null){
            authByToken();
        }
        setContentView(R.layout.activity_loginning);


        ButterKnife.bind(this);
        mRememberPswd.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
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

                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Всё пропало Шеф!!!");
                    }
                }

                @Override
                public void onFailure(Call<UserRes> call, Throwable t) {
                    //TODO 11.07.2016 обработать ошибки
                }
            });
        } else {
            showSnackBar("Сеть на данный момент не доступна, загружаем предыдущие данные");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                signIn();
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;
        }

    }

    private void showSnackBar(String msg) {
        Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(User user) {

        UserInfoUpdateHelper resHelper = new UserInfoUpdateHelper(user);
        try{
        resHelper.saveUserValues();
        resHelper.saveUserFields();
        resHelper.saveUserFio();
        resHelper.saveUserPhoto();
        resHelper.saveUserAvatar();}
        catch (Exception e){

        }
        toMainActivity();
        
    }

    private void toMainActivity(){
        Intent toMainActivity = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(toMainActivity);
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
                        UserModelRes userModel =response.body();
                        showSnackBar(userModel.getData().getToken());
                        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
                        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
                        loginSuccess(userModel.getData().getUser());

                    } else if (response.code() == 404) {
                        showSnackBar("Неверный логин или пароль");
                    } else {
                        showSnackBar("Всё пропало Шеф!!!");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    //TODO 11.07.2016 обработать ошибки
                }
            });
        } else {
            showSnackBar("Сеть на данный момент не доступна, загружаем предыдущие данные");
            toMainActivity();
        }
    }



}