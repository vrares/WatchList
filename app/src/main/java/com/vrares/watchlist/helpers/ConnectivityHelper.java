package com.vrares.watchlist.helpers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.models.User;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;

/**
 * Created by rares.vultur on 11/13/2017.
 */

public class ConnectivityHelper {

    private static final String TAG = "Error";

    private FirebaseAuth auth;
    private RegisterPresenterCallback registerPresenterCallback;

    public void register(User user, String password, RegisterPresenterCallback registerCallback) {
        this.registerPresenterCallback = registerCallback;
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                   todo         registerPresenterCallback.onAccountCreated();
                        } else {
                            Log.d(TAG, "Error");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                   todo     registerPresenterCallback.onAccountCreatedFailure(e);
                    }
                });
    }
}
