package com.vrares.watchlist.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vrares.watchlist.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_et_email) EditText etEmail;
    @BindView(R.id.login_et_password) EditText etPassword;
    @BindView(R.id.login_btn_simple_login)Button btnLogin;
    @BindView(R.id.login_btn_google_login)Button btnGoogleLogin;
    @BindView(R.id.login_btn_facebook_login)Button btnFacebookLogin;
    @BindView(R.id.login_btn_register)Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.login_btn_register)
    private void goToRegisterActivity() {
        //// TODO: 11/13/2017 Go to Register Activity
    }
}
