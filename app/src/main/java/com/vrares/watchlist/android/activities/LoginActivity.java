package com.vrares.watchlist.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.LoginView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.utils.ProgressDialogUtil;
import com.vrares.watchlist.presenters.classes.LoginPresenter;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Scope;
import toothpick.Toothpick;

public class LoginActivity extends AppCompatActivity implements LoginView, GoogleApiClient.OnConnectionFailedListener{

    private static final String LOGING_IN_MESSAGE = "Loging in...";
    private static final int GOOGLE_SIGN_IN = 9001;
    public static final String SHARED_PREF = "sharedPref";
    public static final String EMAIL_PREF =  "emailPref";
    public static final String FIRST_NAME_PREF = "firstNamePref";
    public static final String LAST_NAME_PREF = "lastNamePref";
    public static final String PICTURE_PREF = "picturePref";

    @BindView(R.id.login_et_email) EditText etEmail;
    @BindView(R.id.login_et_password) EditText etPassword;
    @BindView(R.id.login_btn_simple_login)Button btnLogin;
    @BindView(R.id.login_btn_google_login)Button btnGoogleLogin;
    @BindView(R.id.login_btn_facebook_login)Button btnFacebookLogin;
    @BindView(R.id.login_btn_register)Button btnRegister;

    @Inject LoginPresenter loginPresenter;

    private Scope scope;
    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private ProgressDialogUtil progressDialogUtil;
    private CallbackManager callbackManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        initViews();
        setupFacebookLogin();
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

    private void initViews() {
        FacebookSdk.sdkInitialize(this);
        progressDialogUtil = new ProgressDialogUtil(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    @OnClick(R.id.login_btn_register)
    public void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_btn_simple_login)
    public void firebaseLogin() {
        if (etEmail.getText().toString().equals("") ||
                etPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill in all data", Toast.LENGTH_SHORT).show();
        } else {
            progressDialogUtil.showProgressDialog(LOGING_IN_MESSAGE);
            loginPresenter.firebaseLogin(etEmail.getText().toString(), etPassword.getText().toString());
        }
    }

    @OnClick(R.id.login_btn_google_login)
    public void googleLogin() {
        progressDialogUtil.showProgressDialog(LOGING_IN_MESSAGE);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, GOOGLE_SIGN_IN);
    }

    public void setupFacebookLogin() {
        final String[] facebookPermissions = {"public_profile", "email"};

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                btnFacebookLogin.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.facebook_button_animation));
                progressDialogUtil.showProgressDialog("Signing in");
                loginPresenter.facebookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("Cancel", "{cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FacebookError", error.getMessage());
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(facebookPermissions));
            }
        });

    }


    @Override
    public void onSignInSuccess() {
        loginPresenter.getUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Override
    public void onSignInFailed(Exception e) {
        progressDialogUtil.dismiss();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGoogleLoginFailure(Exception e) {
        progressDialogUtil.dismiss();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFacebookLoginFailure(Exception e) {
        progressDialogUtil.dismiss();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserDetailsRetrieved(User user) {
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(EMAIL_PREF, user.getEmail())
                .putString(FIRST_NAME_PREF, user.getFirstName())
                .putString(LAST_NAME_PREF, user.getLastName())
                .putString(PICTURE_PREF, user.getPicture())
                .apply();
        progressDialogUtil.dismiss();
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account= result.getSignInAccount();
                loginPresenter.googleLogin(account);
            }
        }
    }
}
