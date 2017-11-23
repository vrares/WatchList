package com.vrares.watchlist.android.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrares.watchlist.R;
import com.vrares.watchlist.models.adapters.MovieListAdapterCallback;
import com.vrares.watchlist.models.adapters.MovieListAdapter;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.models.pojos.Watcher;
import com.vrares.watchlist.presenters.callbacks.LoginPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.MovieDetailsPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.UserDetailsPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.UserSearchPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.WatchersPresenterCallback;
import com.vrares.watchlist.presenters.classes.UserSearchPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Singleton;

@Singleton
public class DatabaseHelper {

    private static final String USERS_NODE = "users";
    private static final String USER_PICTURE_NODE = "picture";
    private static final String MOVIES_NODE = "movies";
    private static final String SEEN_BY_NODE = "seenBy";
    private static final String SEEN_COUNT = "seenCount";
    private static final String FULL_NAME_NODE = "fullname";
    private static final String TIME = "time";
    private static final String TAG = "Error";
    private static final String EMAIL_NODE = "email";
    private static final String FIRST_NAME_NODE = "firstName";
    private static final String LAST_NAME_NODE = "lastName";
    private static final String PICTURE_NODE = "picture";

    private RegisterPresenterCallback registerPresenterCallback;
    private LoginPresenterCallback loginPresenterCallback;
    private MovieListAdapterCallback movieListAdapterCallback;
    private MovieDetailsPresenterCallback movieDetailsPresenterCallback;
    private UserDetailsPresenterCallback userDetailsPresenterCallback;
    private WatchersPresenterCallback watchersPresenterCallback;
    private UserSearchPresenterCallback userSearchPresenterCallback;

    private DatabaseReference ref;
    private FirebaseAuth auth;

    public void insertUserIntoDatabase(final User user, final RegisterPresenterCallback registerCallback, final LoginPresenterCallback loginCallback, final UserDetailsPresenterCallback userDetailsCallback) {
        this.registerPresenterCallback = registerCallback;
        this.loginPresenterCallback = loginCallback;
        this.userDetailsPresenterCallback = userDetailsCallback;

        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();

                if (userDetailsPresenterCallback != null) {
                    ref.child(auth.getCurrentUser().getUid()).setValue(user);
                    userDetailsPresenterCallback.onUserInsertedSuccess();

                } else {

                    if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                        ref.child(auth.getCurrentUser().getUid()).setValue(user);
                    }
                    if (loginPresenterCallback != null) {
                        loginPresenterCallback.onUserInsertedSuccess();
                    } else if (registerPresenterCallback != null) {
                        registerPresenterCallback.onUserInsertedSuccess();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }

    public void getSeenInformation(final Integer movieId, final MovieListAdapter.MyViewHolder holder, final Movie movie) {
        ref = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(movieId)).child(SEEN_BY_NODE).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    holder.buttonItem.setBackgroundResource(R.drawable.btn_seen);
                } else {
                    holder.buttonItem.setBackgroundResource(R.drawable.btn_unseen);
                }

                if (!dataSnapshot.hasChild(String.valueOf(movieId))) {
                    holder.detailsItem.setText(movie.getReleaseDate());
                } else {
                    String seenCount = dataSnapshot.child(String.valueOf(movieId)).child(SEEN_COUNT).getValue().toString();
                    holder.detailsItem.setText("There are " + seenCount + " other watchers");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void seenButtonAction(final Movie movie, final Context context, MovieListAdapterCallback movieListCallback, final MovieListAdapter.MyViewHolder holder, final int position) {
        this.movieListAdapterCallback = movieListCallback;
        final String movieId = String.valueOf(movie.getId());
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();
                setSeen(movie, holder, position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    private void setSeen(final Movie movie, final MovieListAdapter.MyViewHolder holder, final int position) {
        final int[] operations = new int[2];
        final DatabaseReference movieRef = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FULL_NAME_NODE).getValue().toString();
                String userPicture = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(USER_PICTURE_NODE).getValue().toString();
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FULL_NAME_NODE).setValue(userName);
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(TIME).setValue(getCurrentDate());
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(USER_PICTURE_NODE).setValue(userPicture);
                operations[0] = 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

        movieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            int seenCount;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(String.valueOf(movie.getId()))) {
                    seenCount = dataSnapshot.child(String.valueOf(movie.getId())).child(SEEN_COUNT).getValue(Integer.class);
                    movieRef.child(String.valueOf(movie.getId())).child(SEEN_COUNT).setValue(seenCount + 1);
                } else {
                    movieRef.child(String.valueOf(movie.getId())).child(SEEN_COUNT).setValue(1);
                    seenCount = 1;
                }
                operations[1] = 1;
                if (operations[0] == 1 && operations[1] == 1) {
                    movieListAdapterCallback.onMovieSeen(seenCount, holder, position);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd MMM");
        return date.format(calendar.getTime());
    }

    public void unseeButtonAction(Movie movie, MovieListAdapterCallback movieListCallback, final MovieListAdapter.MyViewHolder holder, final int position) {
        this.movieListAdapterCallback = movieListCallback;
        final String movieId = String.valueOf(movie.getId());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref.child(movieId).child(SEEN_BY_NODE).child(currentUser.getUid()).removeValue();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int seenCount = dataSnapshot.child(movieId).child(SEEN_COUNT).getValue(Integer.class);
                if (seenCount == 1) {
                    ref.child(movieId).removeValue();
                } else {
                    seenCount = seenCount - 1;
                    ref.child(movieId).child(SEEN_COUNT).setValue(seenCount);
                }
                movieListAdapterCallback.onMovieSeen(seenCount, holder, position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getSeenCount(final Movie movie, MovieDetailsPresenterCallback movieDetailsCallback) {
        this.movieDetailsPresenterCallback = movieDetailsCallback;
        ref = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String seenCount;
                if (dataSnapshot.hasChild(String.valueOf(movie.getId()))) {
                    seenCount = dataSnapshot.child(String.valueOf(movie.getId())).child(SEEN_COUNT).getValue().toString();
                } else {
                    seenCount = "0";
                }

                movieDetailsPresenterCallback.onSeenCountReceived(seenCount);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserDetails(final String uid, LoginPresenterCallback loginCallback) {
        this.loginPresenterCallback = loginCallback;
        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child(uid).child(EMAIL_NODE).getValue().toString();
                String firstName = dataSnapshot.child(uid).child(FIRST_NAME_NODE).getValue().toString();
                String lastName = dataSnapshot.child(uid).child(LAST_NAME_NODE).getValue().toString();
                String picture = dataSnapshot.child(uid).child(PICTURE_NODE).getValue().toString();
                User user = new User(firstName, lastName, email, picture);
                loginPresenterCallback.onUserDetailsRetrieved(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getWatchers(Movie movie, final ArrayList<Watcher> watcherList, WatchersPresenterCallback watchersCallback) {
        this.watchersPresenterCallback = watchersCallback;
        ref = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref.child(String.valueOf(movie.getId())).child(SEEN_BY_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String name = snapshot.child(FULL_NAME_NODE).getValue().toString();
                    String picture = snapshot.child(PICTURE_NODE).getValue().toString();
                    String time = snapshot.child(TIME).getValue().toString();
                    Watcher watcher = new Watcher(name, picture, id, time);
                    watcherList.add(watcher);
                }
                watchersPresenterCallback.onWatchersReceived(watcherList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    public void searchForUser(final String userQuery, final ArrayList<User> usersList, UserSearchPresenterCallback userSearchCallback) {
        usersList.clear();
        this.userSearchPresenterCallback = userSearchCallback;
        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child(EMAIL_NODE).getValue().toString().toLowerCase().contains(userQuery) ||
                            snapshot.child(FULL_NAME_NODE).getValue().toString().toLowerCase().contains(userQuery)) {
                        String id = snapshot.getKey();
                        String firstName = snapshot.child(FIRST_NAME_NODE).getValue().toString();
                        String lastName = snapshot.child(LAST_NAME_NODE).getValue().toString();
                        String email = snapshot.child(EMAIL_NODE).getValue().toString();
                        String picture = snapshot.child(PICTURE_NODE).getValue().toString();
                        User user = new User(firstName, lastName, email, picture);
                        user.setId(id);
                        usersList.add(user);
                    }

                }
                userSearchPresenterCallback.onUserSearchComplete(usersList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    public void setPictureFromGallery(Context context, Intent data) {
    }
}
