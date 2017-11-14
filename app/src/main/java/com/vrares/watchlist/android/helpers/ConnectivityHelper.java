package com.vrares.watchlist.android.helpers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.models.User;
import com.vrares.watchlist.presenters.callbacks.LoginPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;

import javax.inject.Singleton;

@Singleton
public class ConnectivityHelper {

    private static final String TAG = "Error";

    private FirebaseAuth auth;
    private RegisterPresenterCallback registerPresenterCallback;
    private LoginPresenterCallback loginPresenterCallback;

    public void register(final User user, String password, RegisterPresenterCallback registerCallback) {
        this.registerPresenterCallback = registerCallback;
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerPresenterCallback.onAccountCreated(user);
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        registerPresenterCallback.onAccountCreatedFailure(e);
                    }
                });
    }

    public void firebaseLogin(String email, String password, final LoginPresenterCallback loginCallback) {
        this.loginPresenterCallback = loginCallback;
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginPresenterCallback.onSignInSuccess();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginPresenterCallback.onSignInFailed(e);
                    }
                });
    }
}
