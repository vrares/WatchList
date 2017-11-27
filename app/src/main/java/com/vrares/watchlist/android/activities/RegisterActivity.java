package com.vrares.watchlist.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.RegisterView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.utils.ProgressDialogUtil;
import com.vrares.watchlist.presenters.classes.RegisterPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.vrares.watchlist.android.activities.LoginActivity.EMAIL_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.FIRST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.LAST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.PICTURE_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.SHARED_PREF;

public class RegisterActivity extends AppCompatActivity implements RegisterView{

    private static final String PICTURE_GENERATOR_LINK = "https://api.adorable.io/avatars/180/";
    private static final String PICTURE_GENERATION_EXTENSION = ".png";
    private static final String REGISTERING_MESSAGE = "Registering...";

    @BindView(R.id.register_et_first_name)EditText firstName;
    @BindView(R.id.register_et_last_name)EditText lastName;
    @BindView(R.id.register_et_email)EditText email;
    @BindView(R.id.register_et_password)EditText password;
    @BindView(R.id.register_et_confirm_password)EditText confirmPassword;
    @BindView(R.id.register_btn_register)Button btnRegister;

    @Inject RegisterPresenter registerPresenter;

    private Scope scope;
    private ProgressDialogUtil progressDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        progressDialogUtil = new ProgressDialogUtil(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerPresenter.attach(this);
    }

    @Override
    protected void onStop() {
        registerPresenter.detach();
        super.onStop();
    }

    @OnClick(R.id.register_btn_register)
    public void register() {
        if (password.getText().toString().equals("") ||
                firstName.getText().toString().equals("") ||
                lastName.getText().toString().equals("")||
                confirmPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill in all data", Toast.LENGTH_SHORT).show();

        } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();

        } else {
            progressDialogUtil.showProgressDialog(REGISTERING_MESSAGE);
            User user = new User(firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    PICTURE_GENERATOR_LINK + email.getText().toString() + PICTURE_GENERATION_EXTENSION);

            registerPresenter.register(user, password.getText().toString());
        }

    }

    @Override
    public void onAccountCreatedFailure(Exception e) {
        progressDialogUtil.dismiss();
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserInsertedSuccess(User user) {
        progressDialogUtil.dismiss();
        Toast.makeText(this, "User created with success", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(EMAIL_PREF, user.getEmail())
                .putString(FIRST_NAME_PREF, user.getFirstName())
                .putString(LAST_NAME_PREF, user.getLastName())
                .putString(PICTURE_PREF, user.getPicture())
                .apply();
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}
