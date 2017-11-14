package com.vrares.watchlist.android.helpers;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrares.watchlist.models.User;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;
import com.vrares.watchlist.presenters.classes.RegisterPresenter;

import javax.inject.Singleton;

@Singleton
public class DatabaseHelper {

    private static final String USERS_NODE = "users";
    private static final String TAG = "Error";

    private RegisterPresenterCallback registerPresenterCallback;
    private DatabaseReference ref;
    private FirebaseAuth auth;

    public void insertUserIntoDatabase(final User user, final RegisterPresenterCallback registerPresenterCallback) {
        this.registerPresenterCallback = registerPresenterCallback;
        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();
                if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                    ref.child(auth.getCurrentUser().getUid()).setValue(user);
                }
                registerPresenterCallback.onUserInsertedSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }
}
