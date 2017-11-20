package com.vrares.watchlist.android.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vrares.watchlist.R;
import com.vrares.watchlist.models.adapters.MovieListAdapterCallback;
import com.vrares.watchlist.models.adapters.MovieListMovieListAdapter;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.LoginPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.MovieDetailsPresenterCallback;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;

import java.text.SimpleDateFormat;
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
    private static final String TIME  = "time";
    private static final String TAG = "Error";

    private RegisterPresenterCallback registerPresenterCallback;
    private LoginPresenterCallback loginPresenterCallback;
    private MovieListAdapterCallback movieListAdapterCallback;
    private MovieDetailsPresenterCallback movieDetailsPresenterCallback;

    private DatabaseReference ref;
    private FirebaseAuth auth;

    public void insertUserIntoDatabase(final User user, final RegisterPresenterCallback registerCallback, final LoginPresenterCallback  loginCallback) {
        this.registerPresenterCallback = registerCallback;
        this.loginPresenterCallback = loginCallback;
        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();
                if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                    ref.child(auth.getCurrentUser().getUid()).setValue(user);
                }
                if (loginPresenterCallback == null) {
                    registerPresenterCallback.onUserInsertedSuccess();
                } else {
                    loginPresenterCallback.onUserInsertedSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }

    public void getSeenInformation(final Integer movieId, final MovieListMovieListAdapter.MyViewHolder holder, final Movie movie) {
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

    public void seenButtonAction(final Movie movie, final Context context, MovieListAdapterCallback movieListCallback, final MovieListMovieListAdapter.MyViewHolder holder, final int position) {
        this.movieListAdapterCallback = movieListCallback;
        final String movieId = String.valueOf(movie.getId());
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = FirebaseAuth.getInstance();
                if (dataSnapshot.hasChild(movieId)) {
                    if (dataSnapshot.child(movieId).child(SEEN_BY_NODE).hasChild(currentUser.getUid())) {
                        Toast.makeText(context, "Movie Already Seen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setSeen(movie, holder, position);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

    private void setSeen(final Movie movie, final MovieListMovieListAdapter.MyViewHolder holder, final int position) {
        final int[] operations = new int[2];
        final DatabaseReference movieRef = FirebaseDatabase.getInstance().getReference(MOVIES_NODE);
        ref = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FULL_NAME_NODE).getValue().toString();
                String userPicture = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(USER_PICTURE_NODE).getValue().toString();
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getUid()).child(FULL_NAME_NODE).setValue(userName);
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getUid()).child(TIME).setValue(getCurrentDate());
                movieRef.child(movie.getId().toString()).child(SEEN_BY_NODE).child(FirebaseAuth.getInstance().getUid()).child(USER_PICTURE_NODE).setValue(userPicture);
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

    public void unseeButtonAction(Movie movie, Context context, MovieListAdapterCallback movieListCallback, MovieListMovieListAdapter.MyViewHolder holder, int position) {
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
}
