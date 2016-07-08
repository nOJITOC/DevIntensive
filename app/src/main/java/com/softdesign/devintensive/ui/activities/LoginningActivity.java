package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Start activity with loginning form
 */
public class LoginningActivity extends AppCompatActivity {

    @BindView(R.id.input_password)
    private EditText inputEmail;
    @BindView(R.id.input_email)
    private EditText inputPassword;
    @BindView(R.id.input_layout_password)
    private TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_layout_email)
    private TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_signup)
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginning);
        ButterKnife.bind(this);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    /**
     * Validating form with intent to MainActivity.class
     */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

/**
 * Starts {@link MainActivity} with delay
 */
        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginningActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 400);
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * TextWatcher for Login form
     */
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
}