package com.vrares.watchlist.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.LoginView;
import com.vrares.watchlist.presenters.classes.LoginPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Scope;
import toothpick.Toothpick;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.login_et_email) EditText etEmail;
    @BindView(R.id.login_et_password) EditText etPassword;
    @BindView(R.id.login_btn_simple_login)Button btnLogin;
    @BindView(R.id.login_btn_google_login)Button btnGoogleLogin;
    @BindView(R.id.login_btn_facebook_login)Button btnFacebookLogin;
    @BindView(R.id.login_btn_register)Button btnRegister;

    @Inject LoginPresenter loginPresenter;
    private Scope scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.attach(this);
    }

    @Override
    protected void onStop() {
        loginPresenter.detach();
        super.onStop();
    }

    @OnClick(R.id.login_btn_register)
    public void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_btn_simple_login)
    public void firebaseLogin() {
        loginPresenter.firebaseLogin(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void onSignInSuccess() {
        //// TODO: 11/14/2017 Redirect to Movie List
    }

    @Override
    public void onSignInFailed(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
