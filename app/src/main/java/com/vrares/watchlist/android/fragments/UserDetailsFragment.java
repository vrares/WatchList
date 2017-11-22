package com.vrares.watchlist.android.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.UserDetailsView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.utils.AlertDialogUtil;
import com.vrares.watchlist.presenters.callbacks.UserDetailsPresenterCallback;
import com.vrares.watchlist.presenters.classes.UserDetailsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.vrares.watchlist.android.activities.LoginActivity.EMAIL_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.FIRST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.LAST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.PICTURE_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.SHARED_PREF;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailsFragment extends Fragment implements UserDetailsView{

    private static final String PICTURE_GENERATOR_LINK = "https://api.adorable.io/avatars/180/";
    private static final String PICTURE_GENERATION_EXTENSION = ".png";


    @Inject UserDetailsPresenter userDetailsPresenter;
    @BindView(R.id.edit_picture)CircleImageView civPicture;
    @BindView(R.id.edit_et_name)EditText etName;
    @BindView(R.id.edit_et_pass)EditText etPass;
    @BindView(R.id.edit_btn_save)Button btnSave;
    @BindView(R.id.edit_pb)ProgressBar pbLoad;

    private Scope scope;
    private User user;
    private User newUser;
    private SharedPreferences sharedPreferences;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        user = new User();
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        user.setEmail(sharedPreferences.getString(EMAIL_PREF, null));
        user.setFirstName(sharedPreferences.getString(FIRST_NAME_PREF, null));
        user.setLastName(sharedPreferences.getString(LAST_NAME_PREF, null));
        user.setPicture(sharedPreferences.getString(PICTURE_PREF, null));
        Glide.with(this)
                .load(PICTURE_GENERATOR_LINK + user.getEmail() + PICTURE_GENERATION_EXTENSION)
                .into(civPicture);
        etName.setText(user.getFullname());


    }

    @OnClick(R.id.edit_btn_save)
    public void updateData() {
        String userName = etName.getText().toString();
        String[] userNameSplitted = userName.split(" ");
        newUser = new User(userNameSplitted[0], userNameSplitted[1], FirebaseAuth.getInstance().getCurrentUser().getEmail(), "default");
        pbLoad.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        if (etPass.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please insert your password for confirmation", Toast.LENGTH_SHORT).show();
            pbLoad.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        } else {
            userDetailsPresenter.updateData(etPass.getText().toString(), newUser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        userDetailsPresenter.attach(this);
    }

    @Override
    public void onStop() {
        userDetailsPresenter.detach();
        super.onStop();
    }

    @Override
    public void onPasswordValidationFailed(Exception exception) {
        pbLoad.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        AlertDialogUtil alertDialogUtil = new AlertDialogUtil(getContext());
        alertDialogUtil.displayAlert("Failure", exception.getMessage());
    }

    @Override
    public void onUserUpdated() {
        Toast.makeText(getContext(), "User Updated", Toast.LENGTH_SHORT).show();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new MovieListFragment())
                .commit();
        sharedPreferences.edit()
                .putString(FIRST_NAME_PREF, newUser.getFirstName())
                .putString(LAST_NAME_PREF, newUser.getLastName())
                .apply();

    }
}
