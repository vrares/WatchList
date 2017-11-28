package com.vrares.watchlist.android.fragments;


import android.content.Context;
import android.content.Intent;
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
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.activities.NavigationActivity;
import com.vrares.watchlist.android.views.UserDetailsView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.utils.AlertDialogCallback;
import com.vrares.watchlist.models.utils.AlertDialogUtil;
import com.vrares.watchlist.presenters.classes.UserDetailsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import toothpick.Scope;
import toothpick.Toothpick;

import static android.app.Activity.RESULT_OK;
import static com.vrares.watchlist.android.activities.LoginActivity.EMAIL_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.FIRST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.LAST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.PICTURE_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.SHARED_PREF;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailsFragment extends Fragment implements UserDetailsView, AlertDialogCallback {

    public static final int GALLERY_CODE = 1;

    @Inject UserDetailsPresenter userDetailsPresenter;
    @BindView(R.id.edit_picture) CircleImageView civPicture;
    @BindView(R.id.edit_et_name) EditText etName;
    @BindView(R.id.edit_btn_save) Button btnSave;
    @BindView(R.id.edit_pb) ProgressBar pbLoad;

    private Scope scope;
    private User user;
    private User newUser;
    private SharedPreferences sharedPreferences;
    private String picturePath;
    private boolean isPictureChanged;

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
        isPictureChanged = false;
        user = new User();
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        user.setEmail(sharedPreferences.getString(EMAIL_PREF, null));
        user.setFirstName(sharedPreferences.getString(FIRST_NAME_PREF, null));
        user.setLastName(sharedPreferences.getString(LAST_NAME_PREF, null));
        user.setPicture(sharedPreferences.getString(PICTURE_PREF, null));
        Glide.with(this)
                .load(sharedPreferences.getString(PICTURE_PREF, null))
                .into(civPicture);
        etName.setText(user.getFullname());


    }

    @OnClick(R.id.edit_btn_save)
    public void updateData() {
        AlertDialogUtil alertDialogUtil = new AlertDialogUtil(getContext());
        alertDialogUtil.displayChoiceAlert("Are you sure you want to update your information?", this);
    }

    @OnClick(R.id.edit_picture)
    public void changeProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            picturePath = data.getData().toString();
            Glide.with(this)
                    .load(picturePath)
                    .into(civPicture);
            isPictureChanged = true;
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
    public void onUserUpdated(User user) {

        Toast.makeText(getContext(), "User Updated", Toast.LENGTH_SHORT).show();
        sharedPreferences.edit()
                .putString(FIRST_NAME_PREF, newUser.getFirstName())
                .putString(LAST_NAME_PREF, newUser.getLastName())
                .putString(PICTURE_PREF, user.getPicture())
                .apply();
        ((NavigationActivity)getActivity()).updateUserDetails();
        pbLoad.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new MovieListFragment())
                .commit();

    }

    @Override
    public void onUserUpdateFailed(Exception exception) {
        new AlertDialogUtil(getContext()).displayAlert("Attention", exception.getMessage());
    }

    @Override
    public void updateUser() {
        String userName = etName.getText().toString();
        if(userName.contains(" ")) {
            String[] userNameSplitted = userName.split(" ");
            if (!isPictureChanged) {
                newUser = new User(userNameSplitted[0], userNameSplitted[1], user.getEmail(), user.getPicture());
            } else {
                newUser = new User(userNameSplitted[0], userNameSplitted[1], user.getEmail(), picturePath);
            }
        } else {
            if (!isPictureChanged) {
                newUser = new User(userName, "", user.getEmail(), user.getPicture());
            } else {
                newUser = new User(userName, "", user.getEmail(), picturePath);
            }
        }
        pbLoad.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        userDetailsPresenter.updateData(newUser);
    }
}
